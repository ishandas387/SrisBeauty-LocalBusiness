package com.srisbeauty.srisbeauty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.srisbeauty.srisbeauty.model.Product;
import com.srisbeauty.srisbeauty.model.ProductViewHolder;
import com.srisbeauty.srisbeauty.model.ishan387.common.Util;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    String categoryName;
    boolean isAdmin;
    StorageReference storage;


    DatabaseReference products;
    Query query =null;
    SimpleDraweeView draweeView ;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    ProgressDialog pd ;

    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestionList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
        if(!Util.isConnectedToInternet(this))
        {
            Toast.makeText(ProductActivity.this, "Offline ! Please check connectivity.",
                    Toast.LENGTH_SHORT  ).show();
            finish();
        }
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                categoryName= null;
                isAdmin = false;

            }
            else {
                categoryName= extras.getString("categoryname");


            }
        } else {
            categoryName= (String) savedInstanceState.getSerializable("categoryname");

        }

        final MaterialSearchBar materialSearchBar;
        products = FirebaseDatabase.getInstance().getReference("Products");
        if(categoryName != null && !categoryName.isEmpty())
        {

          query= products.orderByChild("category").equalTo(categoryName);
        }
        storage = FirebaseStorage.getInstance().getReference();
        materialSearchBar =(MaterialSearchBar) findViewById(R.id.searchbar);
        materialSearchBar.setSpeechMode(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final List<Product> pList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
       DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
              mLayoutManager.getOrientation());
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
              true);*/
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pd = new ProgressDialog(this);
        pd.setMessage("Getting items");
        pd.show();
        loadRecylerView(query);
        materialSearchBar.setLastSuggestions(suggestionList);
           materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for(String search :suggestionList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if(!enabled)
                {
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        pd.hide();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(ProductActivity.this,Cart.class);
                startActivity(cartIntent);
            }
        });
    }
    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.productlistrow,ProductViewHolder.class,products.orderByChild("name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.category.setText(model.getCategory());
                viewHolder.price.setText(Float.toString(model.getPrice()));
                viewHolder.title.setText(model.getName());
                suggestionList.add(model.getName());
                if(null != model.getImageUrl() && !model.getImageUrl().isEmpty())
                {
                    // Picasso.with(getBaseContext()).cancelRequest(viewHolder.bgi);
                    // Picasso.with(getBaseContext()).load(Uri.parse(model.getImageUrl())).into(viewHolder.bgi);
                    Uri uri = Uri.parse(model.getImageUrl());
                    if(null!= uri) {
                        Glide.with(getBaseContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.bgi);
                        // draweeView.setImageURI(Uri.parse(model.getImageUrl()));
                    }
                }

                final Product m = model;
                viewHolder.setItemClickListener(new onClickInterface() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ProductActivity.this, m.getName(),
                                Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ProductActivity.this,ItemDetail.class);
                        i.putExtra("productId",searchAdapter.getRef(position).getKey());
                        i.putExtra("pName", m.getName());
                        startActivity(i);
                    }
                });
            }


        };
        recyclerView.setAdapter(searchAdapter);


    }
    private void loadRecylerView(Query query) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,R.layout.productlistrow,ProductViewHolder.class,query) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.category.setText(model.getCategory());
                viewHolder.price.setText("₹" +Float.toString(model.getPrice()));
                viewHolder.title.setText(model.getName());
                suggestionList.add(model.getName());
                if(null != model.getImageUrl() && !model.getImageUrl().isEmpty())
                {
                    // Picasso.with(getBaseContext()).cancelRequest(viewHolder.bgi);
                    // Picasso.with(getBaseContext()).load(Uri.parse(model.getImageUrl())).into(viewHolder.bgi);
                    Uri uri = Uri.parse(model.getImageUrl());
                    if(null!= uri) {
                        Glide.with(getBaseContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.bgi);
                        // draweeView.setImageURI(Uri.parse(model.getImageUrl()));
                    }
                }

                final Product m = model;
                viewHolder.setItemClickListener(new onClickInterface() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ProductActivity.this, m.getName(),
                                Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ProductActivity.this,ItemDetail.class);
                        i.putExtra("productId",adapter.getRef(position).getKey());
                        i.putExtra("pName", m.getName());
                        startActivity(i);
                    }
                });



            }
        };
        recyclerView.setAdapter(adapter);
    }

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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
