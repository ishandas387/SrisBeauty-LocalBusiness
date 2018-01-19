package com.srisbeauty.srisbeauty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.srisbeauty.srisbeauty.model.MyDailogueFragment;
import com.srisbeauty.srisbeauty.model.OrderHistoryViewHolder;
import com.srisbeauty.srisbeauty.model.OrderItem;
import com.srisbeauty.srisbeauty.model.Orders;
import com.srisbeauty.srisbeauty.model.Users;
import com.srisbeauty.srisbeauty.model.ishan387.db.UserDatabase;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User hub, Address mdification, order history
 */
public class UserHub extends AppCompatActivity {

    TextView username,useremail,userhuaddress;
    FloatingActionButton edit ;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    DatabaseReference orders;
    FirebaseUser user;
    CollapsingToolbarLayout clayout;
    FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder> adapter;
    MyDailogueFragment fragment = new MyDailogueFragment();
    de.hdodenhof.circleimageview.CircleImageView profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhub);

        mAuth = FirebaseAuth.getInstance();
        username = (TextView) findViewById(R.id.username);
        useremail = (TextView) findViewById(R.id.useremail);
        userhuaddress = (TextView) findViewById(R.id.userhuaddress);
        recyclerView = (RecyclerView) findViewById(R.id.order_history_recycler_view);
        de.hdodenhof.circleimageview.CircleImageView profile = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        Query filter = null;
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            if(user.getDisplayName() != null && !user.getDisplayName().isEmpty())
            {
                username.setText(user.getDisplayName());
            }
            else
            {
                username.setText(user.getEmail().split("@")[0]);
            }


            useremail.setText(user.getEmail());
            if(user.getPhotoUrl() != null)
            {
                String url = user.getPhotoUrl().toString();
                if(null != url && !url.isEmpty())
                {

                    Glide.with(this).load(url)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profile);
                }
            }
        }
        orders = FirebaseDatabase.getInstance().getReference("Orders");
        filter = orders.orderByChild("email").equalTo(user.getEmail());
        edit = (FloatingActionButton) findViewById(R.id.editupdate);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHub.this,UserAdd.class);
                startActivity(i);
            }
        });
        Users u = new Users();
        List<Users> userDetails = new UserDatabase(this).getUser();
        if(userDetails!= null && !userDetails.isEmpty())
         u = userDetails.get(0);
        if(null != u.getNm())
        username.setText(u.getNm());
        String city="",near="",addAt="",number="";
        if(null != u.getAddCity() )
        {
            city =  u.getAddCity();
        }
        if(null != u.getAddNear())
        {
            near = u.getAddNear();
        }
        if(null != u.getAddAt())
        {
            addAt = u.getAddAt();
        }
        if(null !=u.getNu() )
        {
            number = u.getNu();
        }
        String storedAdd = number+"\n"+addAt+", "+near+"\n"+city;
        if(storedAdd.equalsIgnoreCase("\n, \n"))
        {
            userhuaddress.setText("Please add your address..");
            userhuaddress.setTextColor(getResources().getColor(R.color.fadedtype));
        }
        else
        {

             userhuaddress.setText(storedAdd);
        }
            loadRecylerView(filter);
        clayout = (CollapsingToolbarLayout) findViewById(R.id.clayout);

        clayout.setExpandedTitleTextAppearance(R.style.expandclayout);
        clayout.setCollapsedTitleTextAppearance(R.style.collapsedclayout);



    }

    private void loadRecylerView(Query filter) {
        adapter = new FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder>(Orders.class,R.layout.orderhistorysinglerow,OrderHistoryViewHolder.class,filter) {
            @Override
            protected void populateViewHolder(final OrderHistoryViewHolder viewHolder, final Orders model, int position) {

                viewHolder.orderTime.setText(model.getServiceTime());
                viewHolder.orderId.setText(model.getOrderId());
                viewHolder.orderPrice.setText("₹ "+model.getTotal());

                String statusSet="Placed";

                if (model.getStatus() == 0)
                {
                    statusSet="Placed";
                    viewHolder.addToCal.setVisibility(View.GONE);
                }
                else if(model.getStatus() == 1)
                {
                    statusSet="Confirmed";
                }
                else if (model.getStatus() == 2)
                {
                    statusSet="Rejected";
                    viewHolder.addToCal.setVisibility(View.GONE);
                }
                viewHolder.status.setText(statusSet);
                viewHolder.addToCal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(model.getStatus()==0)
                        {

                        }
                        else if(model.getStatus()==2)
                        {
                            viewHolder.colorLayoutLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primary, null)); //without theme

                        }
                        else
                        {
                            showAlertDialogueForCal(model);
                        }
                    }
                });

                viewHolder.setItemClickListener(new onClickInterface() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        List<OrderItem> pl = model.getProducts();
                        if(pl !=null)
                        {
                            List<String> itemNames = new ArrayList<>();
                            for (OrderItem oi : pl)
                            {
                                itemNames.add(oi.getName());
                            }
                            fragment.setListItems(itemNames);
                            fragment.setAddress(model.getAddress());
                           // fragment.setAddrViewToGone();
                        }

                        fragment.show(getFragmentManager(),"");

                    }
                });
            }


        };
        recyclerView.setAdapter(adapter);
    }
    private void showAlertDialogueForCal(final Orders model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserHub.this);
        alertDialog.setTitle("Appointment Reminder");
        alertDialog.setMessage("Would you like to add this to your calendar?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, "Sri's Beauty appointment - "+model.getOrderId()+":"+model.getUserName()+":"+model.getUserPhoneNumber());
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, model.getAddress());
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, model.getServiceTime()+"--Bill: "+model.getTotal());

                Calendar calendar = Calendar.getInstance();
                String arry [] = model.getServiceTime().split("-");
                int month = getMonthInt(arry[0]);
                String arry2[] = arry[1].split(" @ ");
                int day = Integer.parseInt(arry2[0]);
                String arry3[] = arry2[1].split(":");
                int hour = Integer.parseInt(arry3[0]);
                int min = Integer.parseInt(arry3[1]);
                GregorianCalendar calDate = new GregorianCalendar(calendar.get(Calendar.YEAR), month, day,hour,min);

                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());

                startActivity(calIntent);

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
    private int getMonthInt(String s) {
        switch(s)
        {
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
                return 11;
        }

        return 0;

    }


}
