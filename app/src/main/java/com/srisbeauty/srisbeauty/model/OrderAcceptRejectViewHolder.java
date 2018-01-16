package com.srisbeauty.srisbeauty.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.onClickInterface;


/**
 * Created by ishan on 09-12-2017.
 */

public class OrderAcceptRejectViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView orderId, orderPrice, orderTime,status,customerName,customerPh;
    public ImageButton accept, reject;

    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public OrderAcceptRejectViewHolder(View itemView) {
        super(itemView);
        orderId = (TextView) itemView.findViewById(R.id.orderidacceptreject);
        orderPrice = (TextView) itemView.findViewById(R.id.totalpriceacceptreject);
        orderTime = (TextView) itemView.findViewById(R.id.servicetimeacceptreject);
        status = (TextView) itemView.findViewById(R.id.orderstatusacceptreject);
        customerName = (TextView) itemView.findViewById(R.id.customername);
        customerPh = (TextView) itemView.findViewById(R.id.ph);
        accept =(ImageButton) itemView.findViewById(R.id.acceptbutton);
        reject =(ImageButton) itemView.findViewById(R.id.rejectbutton);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
