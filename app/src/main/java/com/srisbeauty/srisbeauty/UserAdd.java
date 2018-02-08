package com.srisbeauty.srisbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.srisbeauty.srisbeauty.model.Users;
import com.srisbeauty.srisbeauty.model.ishan387.db.UserDatabase;


public class UserAdd extends AppCompatActivity {

    EditText user_name,phone_no,mail_id,add1,add2,add3;

    TextInputLayout name,phone,mail,a1,a2,a3,a4;
    private FirebaseAuth mAuth;

    Button adduser,skip;
   // UserDBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        adduser =(Button) findViewById(R.id.add_user);
        skip =(Button) findViewById(R.id.skip);
       // db = new UserDBHandler(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user_name  = (EditText) findViewById(R.id.input_name);
        phone_no  = (EditText) findViewById(R.id.input_phone);
        mail_id  = (EditText) findViewById(R.id.input_email);
        add1  = (EditText) findViewById(R.id.input_add1);
        add2  = (EditText) findViewById(R.id.input_add2);
        add3  = (EditText) findViewById(R.id.input_add3);
       // pincode  = (EditText) findViewById(R.id.input_add4);
        if(null != user && !user.getEmail().isEmpty())
        {
            mail_id.setText(user.getEmail());
        }

        name = (TextInputLayout) findViewById(R.id.input_layout_name);
        phone= (TextInputLayout) findViewById(R.id.input_layout_phone);
        mail= (TextInputLayout) findViewById(R.id.input_layout_email);
        a1= (TextInputLayout) findViewById(R.id.input_layout_add1);
        a2= (TextInputLayout) findViewById(R.id.input_layout_add2);
        a3= (TextInputLayout) findViewById(R.id.input_layout_add3);
        a4= (TextInputLayout) findViewById(R.id.input_layout_add4);

        user_name.addTextChangedListener(new MyTextWatcher(user_name));
        phone_no.addTextChangedListener(new MyTextWatcher(phone_no));
        mail_id.addTextChangedListener(new MyTextWatcher(mail_id));
        add1.addTextChangedListener(new MyTextWatcher(add1));
        add2.addTextChangedListener(new MyTextWatcher(add2));
        add3.addTextChangedListener(new MyTextWatcher(add3));
       // pincode.addTextChangedListener(new MyTextWatcher(pincode));

        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserAdd.this,UserHub.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void submitForm() {

        if (!validateName()) {
            return;
        }
        else if (!validatePhone()) {
            return;
        }
        else if (!validateEmail()) {
            return;
        }
        else if (!validateAdd1()) {
            return;
        }
        else if (!validateAdd2()) {
            return;
        }
        else if (!validateAdd3()) {
            return;
        }
        /*else if (!validateAdd4()) {
            return;
        }*/
        else
        {
            String a,b,c,d,e,f,g;
            a=user_name.getText().toString();
            b=phone_no.getText().toString();
            c=mail_id.getText().toString();
            d=add1.getText().toString();
            e=add2.getText().toString();
            f=add3.getText().toString();
          //  g=pincode.getText().toString();

            Users user = new Users(a,b,c,d,e,f);
            new UserDatabase(getBaseContext()).addUserDetails(user);
           /* db.deleteAllUser();
            db.addAUser(new Users(1,a,b,c,d,e,f,g));
            Toast.makeText(getApplicationContext(), "Details Added.\nThank You.", Toast.LENGTH_SHORT).show();
*/
            Intent i = new Intent(UserAdd.this,UserHub.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }

    }


    private boolean validateEmail() {
        String email = mail_id.getText().toString().trim();


        if (email.isEmpty() || !isValidEmail(email)) {
            mail.setErrorEnabled(true);
            mail.setError("this email id seems incorrect so far");
            requestFocus(mail_id);
            return false;
        }
        else if(mail_id.length()>mail.getCounterMaxLength()){
            mail.setErrorEnabled(true);
            mail.setError("ohh that long!!!");
            requestFocus(mail_id);
            return false;
        }
        else {
            mail.setErrorEnabled(false);
            return true;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validateName() {
        if (user_name.getText().toString().trim().isEmpty()) {
            name.setErrorEnabled(true);
            name.setError("this field can't be empty");
            requestFocus(user_name);
            return false;
        }
        else if(user_name.length()>name.getCounterMaxLength()){
            name.setErrorEnabled(true);
            name.setError("a rather short name will do");
            requestFocus(user_name);
            return false;
        }
        else {
            name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone() {
        if (phone_no.getText().toString().trim().isEmpty()) {
            phone.setErrorEnabled(true);
            phone.setError("this field can't be empty");
            requestFocus(phone_no);
            return false;
        }
        else if(phone_no.length()>phone.getCounterMaxLength()){
            phone.setErrorEnabled(true);
            phone.setError("a valid one please");
            requestFocus(phone_no);
            return false;
        }
        else if(phone_no.length()!=10){
            phone.setErrorEnabled(true);
            phone.setError("10 digits");
            requestFocus(phone_no);
            return false;
        }
        else {
            phone.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validateAdd1() {
        if (add1.getText().toString().trim().isEmpty()) {
            a1.setErrorEnabled(true);
            a1.setError("this field can't be empty");
            requestFocus(add1);
            return false;
        }
        else if(add1.length()>a1.getCounterMaxLength()){
            a1.setErrorEnabled(true);
            a1.setError("there are more address field below");
            requestFocus(a1);
            return false;
        }
        else {
            a1.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validateAdd2() {
        if (add2.getText().toString().trim().isEmpty()) {
            a2.setErrorEnabled(true);
            a2.setError("this field can't be empty");
            requestFocus(add2);
            return false;
        } else if(add2.length()>a2.getCounterMaxLength()){
            a2.setErrorEnabled(true);
            a2.setError("too long");
            requestFocus(a2);
            return false;
        }
        else {
            a2.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAdd3() {
        if (add3.getText().toString().trim().isEmpty()) {
            a3.setErrorEnabled(true);
            a3.setError("this field can't be empty");
            requestFocus(add3);
            return false;
        } else if(add3.length()>a3.getCounterMaxLength()){
            a3.setErrorEnabled(true);
            a3.setError("oh that's too long..");
            requestFocus(add3);
            return false;
        }
        else {
            a3.setErrorEnabled(false);
            return true;
        }
    }
   /* private boolean validateAdd4() {
       *//* if (pincode.getText().toString().trim().isEmpty()) {
            a4.setErrorEnabled(true);
            a4.setError("this field can't be empty");
            requestFocus(pincode);
            return false;
        } else if(pincode.length()>a4.getCounterMaxLength()){
            a4.setErrorEnabled(true);
            a4.setError("pincodes can't exceed six digits");
            requestFocus(a4);
            return false;
        }
        else if(pincode.length()!=6){
            a4.setErrorEnabled(true);
            a4.setError("6 digits");
            requestFocus(a4);
            return false;
        }
        else {
            a4.setErrorEnabled(false);
            return true;
        }*//*
    }*/



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
                case R.id.input_add1:
                    validateAdd1();
                    break;
                case R.id.input_add2:
                    validateAdd2();
                    break;
                case R.id.input_add3:
                    validateAdd3();
                    break;
              /*  case R.id.input_add4:
                    validateAdd4();
                    break;*/
            }
        }
    }
}
