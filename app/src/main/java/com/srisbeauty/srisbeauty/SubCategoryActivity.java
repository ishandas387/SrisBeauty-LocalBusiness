package com.srisbeauty.srisbeauty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srisbeauty.srisbeauty.model.CategorySelectAdapter;
import com.srisbeauty.srisbeauty.model.ishan387.common.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
String categoryName;
RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        recyclerView =(RecyclerView) findViewById(R.id.subcategoryselect);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                categoryName= null;

            }
            else {
                categoryName= extras.getString("categoryname");


            }
        } else {
            categoryName= (String) savedInstanceState.getSerializable("categoryname");

        }
        if(categoryName != null && !categoryName.isEmpty())
        {
            setTitle(categoryName);
            loadSubcategories(categoryName);
        }
    }

    private void loadSubcategories(String categoryName) {
        String [] subcategory = null ;

        if(categoryName.equalsIgnoreCase(Constants.FACIALS))
        {
             subcategory= getResources().getStringArray(R.array.facials);
        }
        else if(categoryName.equalsIgnoreCase(Constants.HAIR_AND_SKIN_TREATMENTS))
        {
            subcategory= getResources().getStringArray(R.array.HairAndSkinTreatments);

        }
        else if(categoryName.equalsIgnoreCase(Constants.HAIRCUTS_AND_TEXTURES))
        {
            subcategory= getResources().getStringArray(R.array.HaircutsAndTextures);

        }
        else if(categoryName.equalsIgnoreCase(Constants.BRIDAL_PACKAGES))
        {
            subcategory= getResources().getStringArray(R.array.BridalPackages);

        }
        else if(categoryName.equalsIgnoreCase(Constants.HANDS_AND_FEET))
        {
            subcategory= getResources().getStringArray(R.array.HandsAndFeet);

        }

        if(null != subcategory)
        {
            List<String> listOfSubCategories = Arrays.asList(subcategory);
            Collections.sort(listOfSubCategories);
            recyclerView.setAdapter( new CategorySelectAdapter(listOfSubCategories, this,false));
        }
    }
}
