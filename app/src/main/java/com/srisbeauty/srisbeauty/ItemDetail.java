package com.srisbeauty.srisbeauty;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;
import com.srisbeauty.srisbeauty.model.CartItems;
import com.srisbeauty.srisbeauty.model.OrderItem;
import com.srisbeauty.srisbeauty.model.Orders;
import com.srisbeauty.srisbeauty.model.Product;
import com.srisbeauty.srisbeauty.model.Review;
import com.srisbeauty.srisbeauty.model.ReviewAdapter;
import com.srisbeauty.srisbeauty.model.UserDetails;
import com.srisbeauty.srisbeauty.model.Users;
import com.srisbeauty.srisbeauty.model.ishan387.common.Util;
import com.srisbeauty.srisbeauty.model.ishan387.db.CartDatabase;
import com.srisbeauty.srisbeauty.model.ishan387.db.UserDatabase;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Item detail page, User to add to cart, view reviews and add reviews. Admin to edit and delete items.
 */
public class ItemDetail extends AppCompatActivity implements RatingDialogListener {


    TextView name,price,reviewTotal,itemdescription;
    ImageView bgi;
    CollapsingToolbarLayout clayout;
    FloatingActionButton fab;
    String productId,productName;
    FirebaseDatabase products;
    DatabaseReference prod,pastorders;
    Product p;
    RatingBar ratingbar;
    FloatingActionButton ratingButton;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Button adminEdit,adminDelete;
    UserDetails userDetail;
    DataSnapshot users;
    boolean isAdmin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        mAuth = FirebaseAuth.getInstance();

        products = FirebaseDatabase.getInstance();
        prod = products.getReference("Products");
        pastorders = products.getReference("Orders");

        fab = (FloatingActionButton) findViewById(R.id.cart);
        name = (TextView) findViewById(R.id.name);
        price =(TextView) findViewById(R.id.itemdetail_price);
        bgi = (ImageView) findViewById(R.id.backgroundimage);
        ratingButton = (FloatingActionButton) findViewById(R.id.ratingbutton);
        ratingbar = (RatingBar) findViewById(R.id.ratngbar);
        adminDelete =(Button) findViewById(R.id.adminremoveitem);
        adminEdit =(Button) findViewById(R.id.adminedititem);
        reviewTotal = (TextView) findViewById(R.id.reviewtotal);
        itemdescription = (TextView) findViewById(R.id.itemdescription);
        user = mAuth.getCurrentUser();
        if(!Util.isConnectedToInternet(this))
        {
            Toast.makeText(ItemDetail.this, "Offline ! Please check connectivity.",
                    Toast.LENGTH_SHORT  ).show();
            finish();
        }
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRatingDialogue();
            }
        });

        recyclerView =(RecyclerView) findViewById(R.id.reviewlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);


        clayout = (CollapsingToolbarLayout) findViewById(R.id.clayout);

        clayout.setExpandedTitleTextAppearance(R.style.expandclayout);
        clayout.setCollapsedTitleTextAppearance(R.style.collapsedclayout);

        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId");
            productName = getIntent().getStringExtra("pName");

        }
        if (!productId.isEmpty()) {
            getProductDetails();
           /* if(!userHasOrdered(productName,user.getEmail()))
            {
                ratingButton.setEnabled(false);
            }*/

        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
       isAdmin= pref.getBoolean("isAdmin",false);
        if(!isAdmin)
        {
            LinearLayout adminLayout = (LinearLayout)findViewById(R.id.adminsection_itemdetail);
            adminLayout.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceDate = "";
                String serviceTime = "";
                int count = new CartDatabase(getBaseContext()).countOfItem(p.getName());
                if(count ==0)

                {
                    CartItems cartItem = new CartItems(productId, p.getName(), Float.toString(p.getPrice()), serviceDate + "/" + serviceTime);
                    new CartDatabase(getBaseContext()).addToCart(cartItem);
                    Toast.makeText(ItemDetail.this, "Added to cart",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ItemDetail.this, "Already in your cart",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        adminDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
        adminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(p);
            }
        });
    }

    private boolean userHasOrdered( String productName,String email) {
        final List<Orders> userOrders = new ArrayList<>();
        pastorders = FirebaseDatabase.getInstance().getReference("Orders");
        pastorders.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Orders userOrder = postSnapshot.getValue(Orders.class);
                        userOrders.add(userOrder);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(!userOrders.isEmpty())
        {
            List<OrderItem> products = new ArrayList<>();
            for(Orders o : userOrders)
            {
                products.addAll(o.getProducts());
            }
            if(!products.isEmpty())
            {
                for(OrderItem oi :products)
                {
                    if(oi.getName().equalsIgnoreCase(productName))
                    {
                        return true;
                    }
                }
            }
        }
        return false;

    }


    private void editItem(Product p) {

        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("ProductPassed", p);
        i.putExtras(b);
        i.setClass(this, AdminActivity.class);
        startActivity(i);

    }

    private void deleteItem() {
        //delete from db
        prod.child(productId).removeValue();
        //delete image from storage
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        final StorageReference filePathStorage = storage.child("photos").child(productId);
        if(null != filePathStorage)
            filePathStorage.delete();
        Toast.makeText(ItemDetail.this, "Item removed",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showRatingDialogue() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Ok","Good","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate the service")
                .setDescription("Select stars and submit feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setWindowAnimation(R.style.RatingDialogueFadeAnimation)
                .create(ItemDetail.this).show();
    }


    public void getProductDetails() {
        prod.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 p  = dataSnapshot.getValue(Product.class);
                 if(null != p)
                 {
                     if(null!= p.getImageUrl() && !p.getImageUrl().isEmpty())
                         Picasso.with(getBaseContext()).load(Uri.parse(p.getImageUrl())).into(bgi);
                     name.setText(p.getName());
                     price.setText("₹"+p.getPrice());
                     clayout.setTitle(p.getCategory());
                     itemdescription.setText(p.getDescription());
                     List<Review> currentReviewList = p.getReviewList();
                     if(null != currentReviewList && !currentReviewList.isEmpty())
                     {
                         int sum=0, count=0;
                         for(Review r : currentReviewList)
                         {
                             sum = sum + (int) r.getRating();
                             count++;
                         }
                         float average = sum/count;
                         ratingbar.setRating(average);
                         if(count ==1)
                         {

                         reviewTotal.setText(count+" Review");
                         }
                         else if (count>1)
                         {
                             reviewTotal.setText(count+" Reviews");
                         }
                         else
                         {
                             reviewTotal.setText("0 Reviews");
                         }
                         settingListOfReviews(currentReviewList);
                     }
                 }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void settingListOfReviews(List<Review> currentReviewList) {

        ReviewAdapter adapter = new ReviewAdapter(currentReviewList, this,isAdmin);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onPositiveButtonClicked(int i, String s) {

        Review r = new Review();
        r.setReview(s);
        r.setDate(new Date());
        if(null != user)
        {
            if(null != user.getDisplayName() && !user.getDisplayName().isEmpty())
            {

            r.setUserName(user.getDisplayName());
            }
            else
            {
                List<Users> userDetails = new UserDatabase(this).getUser();

                if(userDetails!= null && !userDetails.isEmpty())
                {
                    r.setUserName(userDetails.get(0).getNm());
                }
                else {

                    if(!user.getEmail().isEmpty()) {
                        r.setUserName(user.getEmail());

                    }

                }
            }

            if(!user.getEmail().isEmpty())
            {

                r.setUserEmail(user.getEmail());
            }
            r.setRating(i);

        }
        if (null != p)
        {
            checkReviewExists(p,r);

        }
        else
        {
            getProductDetails();
            checkReviewExists(p,r);
        }
        Toast.makeText(ItemDetail.this, "Thank you for your feedback",
                Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onNegativeButtonClicked() {

    }

    private void checkReviewExists(Product p, Review r) {
        List<Review> reviewList = p.getReviewList();
        boolean flag  =false;
        for( Review review : reviewList)
        {
            if (review.getUserEmail().equalsIgnoreCase(r.getUserEmail()) )
            {
                review.setRating(r.getRating());
                review.setReview(r.getReview());
                review.setDate(new Date());
                //p.getReviewList().add(review);
                prod.child(productId).setValue(p);
                flag = true;
            }

        }
        if(!flag)
        {
            p.getReviewList().add(r);
            prod.child(productId).setValue(p);
        }
    }
}
