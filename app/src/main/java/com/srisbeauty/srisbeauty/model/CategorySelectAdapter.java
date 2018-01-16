package com.srisbeauty.srisbeauty.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.srisbeauty.srisbeauty.ProductActivity;
import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.onClickInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 01-01-2018.
 */

public class CategorySelectAdapter extends RecyclerView.Adapter<CategorySelectViewHolder> {

    private List<String> lisData = new ArrayList<>();
    private Context context;
    boolean isAdmin;

    public CategorySelectAdapter(List<String> lisData, Context context,boolean isAdmin) {
        this.lisData = lisData;
        this.context = context;
        this.isAdmin =isAdmin;
    }
    @Override
    public CategorySelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.categoryselectsingleview,parent,false);
        return new CategorySelectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategorySelectViewHolder holder, int position) {

        holder.categoryName.setText(lisData.get(position));
       holder.setItemClickListener(new onClickInterface() {
           @Override
           public void onClick(View view, int position, boolean isLongClick) {
               getProducts(lisData.get(position),isAdmin);
           }
       });
    }

    private void getProducts(String s, boolean isAdmin) {
        Intent i = new Intent(context,ProductActivity.class);
        i.putExtra("categoryname",s);
        i.putExtra("isAdmin",isAdmin);
        context.startActivity(i);

    }

    @Override
    public int getItemCount() {
        return lisData.size();
    }
}
