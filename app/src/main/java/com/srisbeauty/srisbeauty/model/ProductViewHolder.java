package com.srisbeauty.srisbeauty.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.onClickInterface;


/**
 * Created by ishan on 11-11-2017.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    public TextView title, price, category;
    public RelativeLayout r2 ;
    public ImageView bgi;
    public ImageButton editItem;

    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public ProductViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        category = (TextView) view.findViewById(R.id.category);
        price = (TextView) view.findViewById(R.id.price);
       // r2 = (RelativeLayout) view.findViewById(R.id.layoutphoto) ;
        bgi =(ImageView) view.findViewById(R.id.imphoto);
        //editItem = (ImageButton) view.findViewById(R.id.edititem);
        /*editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
