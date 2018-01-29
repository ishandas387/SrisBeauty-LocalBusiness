package com.srisbeauty.srisbeauty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.srisbeauty.srisbeauty.model.CategorySelectAdapter;
import com.srisbeauty.srisbeauty.model.ishan387.common.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
String categoryName;
RecyclerView recyclerView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.productpagemenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            Intent launchNextActivity;
            launchNextActivity = new Intent(SubCategoryActivity.this, Home.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchNextActivity);
        }
        else if (id == R.id.cart) {
            Intent launchNextActivity;
            launchNextActivity = new Intent(SubCategoryActivity.this, Cart.class);

            startActivity(launchNextActivity);
        }
        return super.onOptionsItemSelected(item);
    }

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
