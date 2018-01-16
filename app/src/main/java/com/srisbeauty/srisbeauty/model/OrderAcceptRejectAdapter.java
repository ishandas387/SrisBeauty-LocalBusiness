package com.srisbeauty.srisbeauty.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.srisbeauty.srisbeauty.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 09-12-2017.
 */

public class OrderAcceptRejectAdapter extends RecyclerView.Adapter<OrderAcceptRejectViewHolder> {

    public OrderAcceptRejectAdapter(List<Orders> lisData, Context context) {
        this.lisData = lisData;
        this.context = context;
    }

    private List<Orders> lisData = new ArrayList<>();
    private Context context;
    @Override
    public OrderAcceptRejectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.acceptrejctsingleview,parent,false);
        return new OrderAcceptRejectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderAcceptRejectViewHolder holder, int position) {
        holder.orderPrice.setText(lisData.get(position).getTotal());
        holder.orderId.setText(lisData.get(position).getTimeStamp());
        holder.orderTime.setText(lisData.get(position).getServiceTime());
        holder.customerName.setText(lisData.get(position).getUserName());
        holder.customerPh.setText(lisData.get(position).getUserPhoneNumber());

        String statusSet="Placed";

        if (lisData.get(position).getStatus() == 0)
        {
            statusSet="Placed";
        }
        else if(lisData.get(position).getStatus() == 1)
        {
            statusSet="Confirmed";
        }
        else if (lisData.get(position).getStatus() == 2)
        {
            statusSet="Rejected";
        }
        holder.status.setText(statusSet);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
