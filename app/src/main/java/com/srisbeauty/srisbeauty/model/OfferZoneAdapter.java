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
 * Created by ishan on 27-12-2017.
 */

public class OfferZoneAdapter  extends RecyclerView.Adapter<OfferZoneViewHolder>  {

    private List<Offers> lisData = new ArrayList<>();
    private Context context;
    public OfferZoneAdapter(List<Offers> lisData, Context context) {
        this.lisData = lisData;
        this.context = context;
    }
    @Override
    public OfferZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.offersinglerow,parent,false);
        return new OfferZoneViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferZoneViewHolder holder, int position) {
        holder.offerzndesc.setText(lisData.get(position).getOfferDescription());
        holder.offerznid.setText(lisData.get(position).getOfferId());
        holder.offerznname.setText(lisData.get(position).getOfferName());
        holder.offerznprice.setText(lisData.get(position).getOfferPrice());

    }

    @Override
    public int getItemCount() {
        return lisData.size();
    }
}
