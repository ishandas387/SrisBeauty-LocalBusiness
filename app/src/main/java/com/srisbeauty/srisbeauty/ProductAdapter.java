package com.srisbeauty.srisbeauty;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.srisbeauty.srisbeauty.model.Product;

import java.util.List;

/**
 * Created by ishan on 09-11-2017.
 */

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

        private List<Product> productList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, price, category;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                category = (TextView) view.findViewById(R.id.category);
                price = (TextView) view.findViewById(R.id.price);
            }
        }


        public ProductAdapter(List<Product> moviesList) {
            this.productList = moviesList;
        }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productlistrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Product product = productList.get(position);
            holder.title.setText(product.getName());
            holder.category.setText(product.getCategory());
            holder.price.setText(Float.toString(product.getPrice()));
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
    }

