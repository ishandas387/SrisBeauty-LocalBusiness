package com.srisbeauty.srisbeauty.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.srisbeauty.srisbeauty.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ishan on 16-12-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private List<Review> lisData = new ArrayList<>();
    private Context context;
    private boolean isAdmin;

    public ReviewAdapter(List<Review> lisData, Context context,boolean isAdmin) {
        this.lisData = lisData;
        this.context = context;
        this.isAdmin =isAdmin;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.reviewsingleview,parent,false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

            holder.reviewRate.setText(Integer.toString((int)lisData.get(position).getRating()));
        Date date = new Date();
        if(null != lisData.get(position).getDate())
            date = lisData.get(position).getDate();
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a ");
            holder.reviewDate.setText(simpleDateformat.format(date));
            if(isAdmin)
            {

                holder.reviewerEmail.setText(lisData.get(position).getUserEmail());
            }
            else
            {
                holder.reviewerEmail.setVisibility(View.GONE);
            }
            holder.reviewDescription.setText(lisData.get(position).getReview());
            holder.reviewerName.setText(lisData.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
      return lisData.size();
    }
}
