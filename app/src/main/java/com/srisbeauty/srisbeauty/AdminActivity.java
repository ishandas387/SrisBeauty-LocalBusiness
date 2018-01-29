package com.srisbeauty.srisbeauty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.srisbeauty.srisbeauty.model.Product;
import com.srisbeauty.srisbeauty.model.ishan387.common.Util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =111 ;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText itemName;
    Spinner category;
    private EditText itemDescription;
    private EditText itemPrice;
    Button save,upload;
    CheckBox hasSubCategories;
    boolean isUpdate = false;

    Product productPassed;

    Uri filePath;
    byte[] bytearray;
    StorageReference storage;

    DatabaseReference products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_activuty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        if(!Util.isConnectedToInternet(this))
        {
            Toast.makeText(AdminActivity.this, "Offline ! Please check connectivity.",
                    Toast.LENGTH_SHORT  ).show();

        }

        itemName = (EditText) findViewById(R.id.field_service);
        itemDescription = (EditText) findViewById(R.id.editText);
        hasSubCategories = (CheckBox) findViewById(R.id.hassubcategories);
        category = (Spinner) findViewById(R.id.categoryspin);
        itemPrice = (EditText) findViewById(R.id.price);
        save = (Button) findViewById(R.id.saveitem);
        upload =(Button) findViewById(R.id.uploadimage);
        products = FirebaseDatabase.getInstance().getReference("Products");
        storage = FirebaseStorage.getInstance().getReference();

       // FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReferenceFromUrl("testlogin-6db22.appspot.com");    //change the url according to your firebase app

        if (getIntent() != null) {
            Bundle b = this.getIntent().getExtras();
            if (b != null)
            {

                productPassed = (Product) b.getSerializable("ProductPassed");
                setViewToEdit(productPassed);
            }


        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUpdate)
                {
                    addItemToDb(view);
                }
                else
                {
                    updateItemToDb(view);
                }


            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });


    }

    private void updateItemToDb(View view) {

        final View v = view;


        final String id = productPassed.getId();
        String[] downloadUrl = new String[3];
        if(bytearray != null)
        {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Updating item..");
            pd.show();

            final StorageReference filePathStorage = storage.child("photos").child(id);
            if(null != filePathStorage)
            filePathStorage.delete();
            UploadTask task = filePathStorage.putBytes(bytearray);
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                    //pd.setMessage("Adding your item, Please wait..");
                }
            });

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String urlD =taskSnapshot.getDownloadUrl().toString();


                    Map<String,Object> pMap = new HashMap<String,Object>();
                    pMap.put("price", Float.parseFloat(itemPrice.getText().toString()));
                    pMap.put("description",itemDescription.getText().toString());
                    pMap.put("name", itemName.getText().toString());
                    if(hasSubCategories.isChecked())
                    {

                        pMap.put("hasSubCategories", true);
                    }
                    else
                    {
                        pMap.put("hasSubCategories", false);
                    }

                    pMap.put("category",category.getSelectedItem().toString());
                    pMap.put("imageUrl",urlD);
                    products.child(id).updateChildren(pMap);
                    //products.child(id).setValue(product);
                    pd.dismiss();
                    Snackbar.make(v, "Product updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

        else {

            Map<String,Object> pMap = new HashMap<String,Object>();
                     pMap.put("price", Float.parseFloat(itemPrice.getText().toString()));
                        pMap.put("description",itemDescription.getText().toString());
                        pMap.put("name", itemName.getText().toString());
                        pMap.put("category",category.getSelectedItem().toString());

                        products.child(id).updateChildren(pMap);
            /*Snackbar.make(v, "Please add image", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
*/
        }
    }

    private void setViewToEdit(Product productPassed) {
        String [] categories = getResources().getStringArray(R.array.type);
        List<String> listOfCategories = Arrays.asList(categories);
        int index = listOfCategories.indexOf(productPassed.getCategory());
        save.setText("Update Item");
        isUpdate = true;
        itemName.setText(productPassed.getName());
        itemPrice.setText(Float.toString(productPassed.getPrice()));
        itemDescription.setText(productPassed.getDescription());
        category.setSelection(index);
        if (productPassed.isHavingSubCategories())
        {
            hasSubCategories.setChecked(true);
        }
        else
        {
            hasSubCategories.setChecked(false);
        }
        upload.setText("Change Image");



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            //progressDialog.show();
            filePath = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bytearray = baos.toByteArray();
            upload.setText("Image selected");
           /* StorageReference filePathStorage = storage.child("photos").child(filePath.getLastPathSegment());
            filePathStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            });*/

        }
    }

    private void addItemToDb(View view) {

        final View v = view;


        final String id = products.push().getKey();
         String[] downloadUrl = new String[3];
        if(bytearray != null && isPriceValid(itemPrice.getText().toString()))
        {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Adding item..");
            pd.show();

            final StorageReference filePathStorage = storage.child("photos").child(id);
            UploadTask task = filePathStorage.putBytes(bytearray);
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                    //pd.setMessage("Adding your item, Please wait..");
                }
            });

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String urlD =taskSnapshot.getDownloadUrl().toString();
                    Product product = new Product();
                    product.setId(id);
                    product.setCategory(category.getSelectedItem().toString());
                    product.setPrice(Float.parseFloat(itemPrice.getText().toString()));
                    product.setName(itemName.getText().toString());
                    product.setDescription(itemDescription.getText().toString());
                    product.setImageUrl(urlD);
                    if(hasSubCategories.isChecked())
                    {

                        product.setHavingSubCategories(true);
                    }
                    else
                    {
                        product.setHavingSubCategories(false);
                    }

                    products.child(id).setValue(product);
                    pd.dismiss();
                    Snackbar.make(v, "Product Added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    itemDescription.setText("");
                    itemName.setText("");
                    itemPrice.setText("");
                    upload.setText("Select Image");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

        else {

            Snackbar.make(v, "Please add image / a valid price", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private boolean isPriceValid(String s) {

        if(s == null || s.isEmpty() )
        {
            return false;
        }

        try{
            Float.parseFloat(s);
            return true;
        }
        catch(NumberFormatException ne)
        {
            return false;
        }

    }

}
