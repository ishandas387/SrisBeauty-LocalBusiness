package com.srisbeauty.srisbeauty.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.onClickInterface;


/**
 * Created by ishan on 12-11-2017.
 */

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView item, price;
    public ImageButton deleteButton;
    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public CartViewHolder(View itemView) {
        super(itemView);
        item = (TextView) itemView.findViewById(R.id.itemname);
        price = (TextView) itemView.findViewById(R.id.itemprice);
        deleteButton =(ImageButton) itemView.findViewById(R.id.deletebutton);
    }

    @Override
    public void onClick(View v) {

    }
}

