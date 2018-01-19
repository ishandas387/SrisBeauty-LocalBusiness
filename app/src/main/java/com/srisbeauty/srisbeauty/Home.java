package com.srisbeauty.srisbeauty;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.srisbeauty.srisbeauty.model.CategorySelectAdapter;
import com.srisbeauty.srisbeauty.model.Offers;
import com.srisbeauty.srisbeauty.model.Product;
import com.srisbeauty.srisbeauty.model.ProductViewHolder;
import com.srisbeauty.srisbeauty.model.UserDetails;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Home content holder. Has category selection and navigation menu drawer. Added offer banner.
 */

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
            private NavigationView navigationView;
            private DrawerLayout drawer;
            private View navHeader;
            private ImageView imgProfile;
            TextView username,useremail;
            private FirebaseAuth mAuth;
            private RecyclerView recyclerView;
            private ProductAdapter mAdapter;
            String url;
            StorageReference storage;
            UserDetails userDetail ;
            DatabaseReference users,offers;
            SliderLayout offerImageLayout;
            boolean isAdmin =false;
            HashMap<String,String> url_maps = new HashMap<String, String>();
            SimpleDraweeView draweeView ;
            FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
            ProgressDialog pd ;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                mAuth = FirebaseAuth.getInstance();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                navHeader = navigationView.getHeaderView(0);
                imgProfile = (ImageView) navHeader.findViewById(R.id.imageprofile);
                // SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.imphoto);
                offerImageLayout = (SliderLayout) findViewById(R.id.imageoffersllider);

                username =(TextView) navHeader.findViewById(R.id.name);
                useremail = (TextView) navHeader.findViewById(R.id.useremail);
                recyclerView =(RecyclerView) findViewById(R.id.categoryselect);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                String [] categories = getResources().getStringArray(R.array.categorytoshow);
                List<String> listOfCategories = Arrays.asList(categories);
                Collections.sort(listOfCategories);
                recyclerView.setAdapter( new CategorySelectAdapter(listOfCategories, this,isAdmin) );

                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();
                    if(extras == null) {
                        url= null;

                    }
                    else {
                        url= extras.getString("userphotourl");

                    }
                } else {
                    url= (String) savedInstanceState.getSerializable("userphotourl");

                }
                // String url = getIntent().getStringExtra("userphotourl");
                loadNavHeader(url,mAuth);


                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cartIntent = new Intent(Home.this,Cart.class);
                        startActivity(cartIntent);
                    }
                });

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
                users = FirebaseDatabase.getInstance().getReference("Users");
                offers = FirebaseDatabase.getInstance().getReference("Offers");
                offers.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("Child ", dataSnapshot.getKey());
                        Log.d("value ", dataSnapshot.getValue().toString());
                        Offers ot = dataSnapshot.getValue(Offers.class);
                       /* Iterable<DataSnapshot> x = dataSnapshot.getChildren();
                        for (DataSnapshot d : x)
                        {
                          */

                        //Offers o = (Offers) d.child(d.getKey()).getValue();
                        if (ot.getUrl() != null && !ot.getUrl().isEmpty())
                            url_maps.put(ot.getOfferName(),ot.getUrl());
                        //}
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                offers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(url_maps!= null && !url_maps.isEmpty())
                        {
                            loadBannerWithOfferImages();
                        }
                        else
                        {
                            offerImageLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                //set up admin menu;
                setUpAdmin();
                offerImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),OfferManagerment.class);
                        startActivity(i);

                    }
                });
                //offerImageLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                offerImageLayout.setDuration(5000);

                LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,
                        new IntentFilter("tokenReceiver"));

            }

    private void loadBannerWithOfferImages() {


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            Log.d("keyname",name);
            Log.d("keyvalue",url_maps.get(name));
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
          /*  textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);*/

            offerImageLayout.addSlider(textSliderView);
        }

    }

    private void setUpAdmin() {


                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                Log.d("",mAuth.getCurrentUser().getUid().toString());
                if(null!= mAuth.getCurrentUser())
                {
                    users.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            userDetail= dataSnapshot.getValue(UserDetails.class);
                            if(null != userDetail )

                            {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Menu nav_Menu = navigationView.getMenu();
                                if(!userDetail.isAdminstrator())
                                {

                                    nav_Menu.findItem(R.id.nav_admin).setVisible(false);
                                }
                                else
                                {
                                    nav_Menu.findItem(R.id.nav_admin).setVisible(true);
                                }
                                editor.putBoolean("isAdmin", userDetail.isAdminstrator());
                                editor.commit();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            if(token != null)
            {
                Map<String,Object> pMap = new HashMap<String,Object>();
                pMap.put("key", token);
                users.child(mAuth.getCurrentUser().getUid()).updateChildren(pMap);
                //send token to your server or what you want to do
            }

        }
    };
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contactus) {
            Intent i = new Intent(getApplicationContext(),CustomerCare.class);
            startActivity(i);
        }
        else if (id == R.id.action_profile) {
            Intent i = new Intent(getApplicationContext(),UserHub.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.offerzone) {
            Intent i = new Intent(getApplicationContext(),OfferManagerment.class);
            startActivity(i);

        }
        else if(id == R.id.gottocart)
        {
            Intent i = new Intent(getApplicationContext(),Cart.class);
            startActivity(i);
        }

        else if (id == R.id.nav_slideshow) {

            mAuth.signOut();
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_admin) {
            Intent i = new Intent(getApplicationContext(),AdminMenu.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sri's Beauty");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        } else if (id == R.id.nav_send) {
            String url = "http://www.facebook.com";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
           /* Intent i=  getOpenFacebookIntent(
                    this
            );*/


        }
        else if (id ==R.id.category)
        {
            Intent i = new Intent(getApplicationContext(),UserHub.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    //searchbar
  /*  FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestionList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;*/

   /* private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;*/
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {

        products = FirebaseDatabase.getInstance().getReference("Products");
        storage = FirebaseStorage.getInstance().getReference();
     */   // Navigation view header

        // txtName = (TextView) navHeader.findViewById(R.id.name);
        //txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        //imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

    @Override
    protected void onStop() {
        offerImageLayout.stopAutoCycle();
        super.onStop();
    }



    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Would like to..");
        final int i;
        final Button delete = new Button(Home.this);
        final Button edit = new Button(Home.this);
        delete.setText("Delete");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "pela",
                        Toast.LENGTH_SHORT).show();
                    //i = 1;
            }
        });
        delete.setText("Edit");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "edit",
                        Toast.LENGTH_SHORT).show();

            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        delete.setLayoutParams(lp);

        edit.setLayoutParams(lp);
        alertDialog.setView(delete);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              /*  if(null != editText.getText() && !editText.getText().toString().isEmpty())
                {
                    placeorderMethod(editText.getText().toString());
                }
                else
                {
                    showAlertDialog();
                }*/
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadNavHeader(String url, FirebaseAuth mAuth) {
        // Loading profile image
        if(null != url && !url.isEmpty())
        {

            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.75f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
        else
        {
            imgProfile.setBackgroundResource(R.mipmap.ic_launcher);

        }
       username.setText( mAuth.getCurrentUser().getDisplayName());
       useremail.setText(mAuth.getCurrentUser().getEmail());
    }


   /* public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/<id_here>"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/<user_name_here>"));
        }
    }*/
}
