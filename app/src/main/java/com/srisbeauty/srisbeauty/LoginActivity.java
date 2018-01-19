package com.srisbeauty.srisbeauty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.srisbeauty.srisbeauty.model.UserDetails;
import com.srisbeauty.srisbeauty.model.ishan387.common.Util;


/**
 * Login activity, email and gmail account support. Create account
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,  GoogleApiClient.OnConnectionFailedListener  {

    private static final String TAG = "EmailPassword";
    private static final String TAG_GOOGLE = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressBar bar;

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    private boolean isUserAddedToTable= false;


    DatabaseReference users;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        pref =  getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        isUserAddedToTable   = pref.getBoolean("isUserAdded",false);
        // Views
      /*  mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
*/        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        //new ProgressTask().execute();

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
       /* findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();


        users = FirebaseDatabase.getInstance().getReference("Users");
        // [END initialize_auth]

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        pref =  getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(null != currentUser)
        {
            if(!pref.getBoolean("isUserAdded",false))
            {
                adduserToUserTable(currentUser);
            }
            Intent i = new Intent(getApplicationContext(),Home.class);
            i.putExtra("username", currentUser.getDisplayName());
            if(null != currentUser.getPhotoUrl())
            {

                i.putExtra("userphotourl", currentUser.getPhotoUrl().toString());
                Log.d("photo url", currentUser.getPhotoUrl().toString());
            }
            if(null != currentUser.getEmail()) {

                i.putExtra("useremail", currentUser.getEmail().toString());
            }
            startActivity(i);
        }


    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
           /* mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
*/
           // findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
           // findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

           // findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
           /* mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);
*/
            //findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

       // showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Verification email sent to your mail id",
                                    Toast.LENGTH_LONG  ).show();
                            sendEmailVerification();
                           /* Intent i = getIntent();
                            finish();
                            startActivity(i);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = getIntent();
                            finish();
                            startActivity(i);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(mAuth.getCurrentUser().isEmailVerified())
                                    {
                                        if(!isUserAddedToTable)
                                        {
                                            adduserToUserTable(user);
                                        }
                                        Intent i = new Intent(getApplicationContext(),Home.class);
                                        i.putExtra("username", user.getDisplayName());

                                        i.putExtra("useremail",user.getEmail().toString());
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Please verify your email",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // [START_EXCLUDE]
                                if (!task.isSuccessful()) {
                                    //   mStatusTextView.setText(R.string.auth_failed);
                                }
                                //hideProgressDialog();
                                // [END_EXCLUDE]
                            }
                        });




       // showProgressDialog();

        // [START sign_in_with_email]

        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        //findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                      //  findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required..");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required..");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }


    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Welcome.."+user.getDisplayName(),
                                    Toast.LENGTH_SHORT).show();
                            if(!isUserAddedToTable)
                            {
                                 adduserToUserTable(user);
                            }
                            Intent i = new Intent(getApplicationContext(),Home.class);
                            i.putExtra("username", user.getDisplayName());
                            i.putExtra("userphotourl", user.getPhotoUrl().toString());
                            Log.d("photo url", user.getPhotoUrl().toString());
                            i.putExtra("useremail",user.getEmail());
                            startActivity(i);
                        //    updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                       // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void adduserToUserTable(final FirebaseUser user) {

        users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null &&
                        dataSnapshot.getChildren().iterator().hasNext()){
                   Log.d("user","user exists in table");
                }else {
                    UserDetails userDetails = new UserDetails(user.getEmail(),false,user.getDisplayName(),user.getUid(), FirebaseInstanceId.getInstance().getToken());
                    if(userDetails.getUserName() == null)
                    {
                        userDetails.setUserName(userDetails.getUserEmail().split("@")[0]);
                    }
                    userDetails.setKey(FirebaseInstanceId.getInstance().getToken()  );
                    users.child(user.getUid()).setValue(userDetails);


                    editor.putBoolean("isUserAdded", true);
                    editor.commit();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            if(Util.isConnectedToInternet(this))
            {

            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Offline ! Please check connectivity.",
                        Toast.LENGTH_SHORT  ).show();

            }
        } else if (i == R.id.email_sign_in_button) {
            if (Util.isConnectedToInternet(this)) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Offline ! Please check connectivity.",
                        Toast.LENGTH_SHORT  ).show();
            }
        }
        else if (i == R.id.sign_in_button) {
            if (Util.isConnectedToInternet(this)) {

                signInWithGoogle();
            }
            else {
                Toast.makeText(getApplicationContext(), "Offline ! Please check connectivity.",
                        Toast.LENGTH_SHORT  ).show();
            }
            /*else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }*/
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
