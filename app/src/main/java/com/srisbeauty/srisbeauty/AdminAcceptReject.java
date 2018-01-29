package com.srisbeauty.srisbeauty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.srisbeauty.srisbeauty.model.MyDailogueFragment;
import com.srisbeauty.srisbeauty.model.OrderAcceptRejectViewHolder;
import com.srisbeauty.srisbeauty.model.OrderItem;
import com.srisbeauty.srisbeauty.model.Orders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Admin to accept/reject orders here.
 */
public class AdminAcceptReject extends AppCompatActivity {

    TextView username,useremail;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    DatabaseReference orders;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Orders, OrderAcceptRejectViewHolder> adapter;
    MyDailogueFragment fragment = new MyDailogueFragment();

    //searchbar
    FirebaseRecyclerAdapter<Orders, OrderAcceptRejectViewHolder> searchAdapter;
    List<String> suggestionList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accept_reject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.acceptrejectorderview);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        orders = FirebaseDatabase.getInstance().getReference("Orders");
        Query q = orders.orderByKey();
        loadRecycler(q);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orders = FirebaseDatabase.getInstance().getReference("Orders");
                Query q = orders.orderByChild("status").equalTo(0);
                loadRecycler(q);
                Snackbar.make(view, "showing only pending orders", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadRecycler(Query q) {



        adapter = new FirebaseRecyclerAdapter<Orders, OrderAcceptRejectViewHolder>(Orders.class,R.layout.acceptrejctsingleview,OrderAcceptRejectViewHolder.class,q) {

            @Override
            protected void populateViewHolder(final OrderAcceptRejectViewHolder viewHolder, final Orders model, int position) {

                viewHolder.orderTime.setText(model.getServiceTime());
                viewHolder.orderId.setText(model.getOrderId());
                viewHolder.orderPrice.setText("₹ "+model.getTotal());
                viewHolder.customerName.setText(model.getUserName());
                viewHolder.customerPh.setText(model.getUserPhoneNumber());
                String statusSet="Placed";

                if (model.getStatus() == 0)
                {
                    statusSet="Placed";
                }
                else if(model.getStatus() == 1)
                {
                    statusSet="Confirmed";
                }
                else if (model.getStatus() == 2)
                {
                    statusSet="Rejected";
                }
                else if (model.getStatus() == 4)
                {
                    statusSet="Cancelled";
                }
                viewHolder.status.setText(statusSet);
                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getStatus()==1 )
                        {
                            Toast.makeText(AdminAcceptReject.this, "It's already confirmed",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(model.getStatus()==2)
                        {
                            Toast.makeText(AdminAcceptReject.this, "It's already rejected",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(model.getStatus()==4)
                        {
                            Toast.makeText(AdminAcceptReject.this, "It's already cancelled",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            model.setStatus(1);
                            orders.child(model.getOrderId()).setValue(model);
                            showAlertDialogueForCal(model);
                        }

                        //toast
                    }
                });

                viewHolder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getStatus()==2)
                        {
                            Toast.makeText(AdminAcceptReject.this, "It's already rejected",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if (model.getStatus() ==1)
                        {
                            Toast.makeText(AdminAcceptReject.this, "It's already confirmed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(model.getStatus()==4)
                        {
                            Toast.makeText(AdminAcceptReject.this, "It's already cancelled",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else
                        {

                            model.setStatus(2);
                            orders.child(model.getOrderId()).setValue(model);
                        }
                        //toast
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
                        }

                        fragment.show(getFragmentManager(),"");

                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);

    }

    private void showAlertDialogueForCal(final Orders model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAcceptReject.this);
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
