package com.srisbeauty.srisbeauty.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.srisbeauty.srisbeauty.CircleTransform;
import com.srisbeauty.srisbeauty.ProductActivity;
import com.srisbeauty.srisbeauty.R;
import com.srisbeauty.srisbeauty.SubCategoryActivity;
import com.srisbeauty.srisbeauty.model.ishan387.common.Constants;
import com.srisbeauty.srisbeauty.onClickInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ishan on 01-01-2018.
 */

public class CategorySelectAdapter extends RecyclerView.Adapter<CategorySelectViewHolder> {

    private List<String> lisData = new ArrayList<>();
    private Context context;
    boolean isAdmin;
    Map<String, String> categoryImages = new HashMap<>();
    Context ctx;

    public CategorySelectAdapter(List<String> lisData, Context context,boolean isAdmin) {
        this.lisData = lisData;
        this.context = context;
        this.isAdmin =isAdmin;
        this.ctx =context.getApplicationContext();
        //load map for category related banners

        categoryImages.put(Constants.WAXING,Integer.toString(R.drawable.waxingbanner));
        categoryImages.put(Constants.HAIR_AND_SKIN_TREATMENTS,Integer.toString(R.drawable.hairtreatmentbanner));
        categoryImages.put(Constants.HANDS_AND_FEET,Integer.toString(R.drawable.handsandfeetbanner));
        categoryImages.put(Constants.HAIRCUTS_AND_TEXTURES,Integer.toString(R.drawable.haircuttexture));
        categoryImages.put(Constants.BRIDAL_PACKAGES,Integer.toString(R.drawable.bridalbanner));
        categoryImages.put(Constants.MASSAGES,Integer.toString(R.drawable.massagebanner));
        categoryImages.put(Constants.LOTUS_PROFESSIONAL,Integer.toString(R.drawable.lpbanner));
        categoryImages.put(Constants.RAAGA_PROFESSIONAL,Integer.toString(R.drawable.ragapbanner));
        categoryImages.put("Masks",Integer.toString(R.drawable.masksbanner));
        categoryImages.put("Face Clean Up",Integer.toString(R.drawable.facecleanupbanner));
        categoryImages.put("Threading",Integer.toString(R.drawable.threadingbanner));
        categoryImages.put("Bleach",Integer.toString(R.drawable.bleachbanner));
        categoryImages.put("Skin Treatment",Integer.toString(R.drawable.skintreatmentbanner));
        categoryImages.put("Hair Colour",Integer.toString(R.drawable.haircolorbanner));
        categoryImages.put("Hair Spa",Integer.toString(R.drawable.hairspabanner));
        categoryImages.put("Manicure",Integer.toString(R.drawable.manicurebanner));
        categoryImages.put("Nail Art",Integer.toString(R.drawable.handsandfeetbanner));
        categoryImages.put("Mehendi",Integer.toString(R.drawable.mehendibanner));
        categoryImages.put("Pedicure",Integer.toString(R.drawable.pedicurebanner));
        categoryImages.put("Hair cuts",Integer.toString(R.drawable.haircutbanner));
        categoryImages.put("Hair Styles",Integer.toString(R.drawable.hairstylingbanner));
        categoryImages.put("Saree Draping",Integer.toString(R.drawable.sareedrappingbanner));
        categoryImages.put("Make up",Integer.toString(R.drawable.bridalmakeupbanner));
        categoryImages.put("Premium Facials",Integer.toString(R.drawable.premiumfacialbanner));
        categoryImages.put("Hair Treatment",Integer.toString(R.drawable.hairstylingbanner));

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
        if(categoryImages.containsKey(lisData.get(position)))
        {
           /* Glide.with(this).load(R.drawable.Integer.parseInt(categoryImages.get(lisData.get(position))))
                    .crossFade()
                    .thumbnail(0.75f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);*/
           Glide.with(ctx).load(Integer.parseInt(categoryImages.get(lisData.get(position)))).diskCacheStrategy(DiskCacheStrategy.ALL)
                   .into(holder.banner);
            //holder.banner.setImageResource(Integer.parseInt(categoryImages.get(lisData.get(position))));
        }
       holder.setItemClickListener(new onClickInterface() {
           @Override
           public void onClick(View view, int position, boolean isLongClick) {
               getProducts(lisData.get(position),isAdmin);
           }
       });
    }

    private void getProducts(String s, boolean isAdmin) {
        if(s.equalsIgnoreCase(Constants.FACIALS))
        {
            Intent i = new Intent(context,SubCategoryActivity.class);
            i.putExtra("categoryname",s);
            context.startActivity(i);
        }
        else if(s.equalsIgnoreCase(Constants.HAIR_AND_SKIN_TREATMENTS))
        {
            Intent i = new Intent(context,SubCategoryActivity.class);
            i.putExtra("categoryname",s);
            context.startActivity(i);
        }
        else if(s.equalsIgnoreCase(Constants.HANDS_AND_FEET))
        {
            Intent i = new Intent(context,SubCategoryActivity.class);
            i.putExtra("categoryname",s);
            context.startActivity(i);
        }
        else if(s.equalsIgnoreCase(Constants.BRIDAL_PACKAGES))
        {
            Intent i = new Intent(context,SubCategoryActivity.class);
            i.putExtra("categoryname",s);
            context.startActivity(i);
        }
        else if(s.equalsIgnoreCase(Constants.HAIRCUTS_AND_TEXTURES))
        {
            Intent i = new Intent(context,SubCategoryActivity.class);
            i.putExtra("categoryname",s);
            context.startActivity(i);
        }
        else
        {
            Intent i = new Intent(context,ProductActivity.class);
            i.putExtra("categoryname",s);
            i.putExtra("isAdmin",isAdmin);
            context.startActivity(i);
        }


    }

    @Override
    public int getItemCount() {
        return lisData.size();
    }
}
