package com.srisbeauty.srisbeauty.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.onClickInterface;


/**
 * Created by ishan on 16-12-2017.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView reviewRate, reviewDescription, reviewerName, reviewerEmail, reviewDate;
    private onClickInterface itemClickListener;

    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ReviewViewHolder(View itemView) {
        super(itemView);
        reviewerName = (TextView) itemView.findViewById(R.id.reviewername);
        reviewDescription = (TextView) itemView.findViewById(R.id.reviewdes);
        reviewerEmail = (TextView) itemView.findViewById(R.id.revieweremail);
        reviewDate = (TextView) itemView.findViewById(R.id.reviewdate);
        reviewRate = (TextView) itemView.findViewById(R.id.ratenumber);
    }

    @Override
    public void onClick(View v) {

    }
}
