package com.srisbeauty.srisbeauty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.srisbeauty.srisbeauty.model.Offers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Add offers here. Offer name,description, price and image.
 */
public class AddOffer extends AppCompatActivity {

    EditText offerName, offrDescription,offerPrice;
    Button addOfferButton,offrImage;
    DatabaseReference offers;
    Uri filePath;
    byte[] bytearray;
    StorageReference storage;

    private static final int PICK_IMAGE_REQUEST =111 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addofferlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        offerName = (EditText) findViewById(R.id.offer_name);
        offerPrice = (EditText) findViewById(R.id.offer_price);
        offrDescription = (EditText) findViewById(R.id.offer_description);
        addOfferButton =(Button) findViewById(R.id.add_offer);
        offrImage =(Button) findViewById(R.id.add_offer_image);
        offers = FirebaseDatabase.getInstance().getReference("Offers");

        storage = FirebaseStorage.getInstance().getReference();
        offrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

            }
        });

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOfferToDb();
            }
        });


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void addOfferToDb() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Adding offer..");
        pd.show();
        final Offers of = new Offers();

        if(bytearray != null)
        {

            final StorageReference filePathStorage = storage.child("offerimages").child(offerName.getText().toString());
            UploadTask task = filePathStorage.putBytes(bytearray);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    of.setOfferId(String.valueOf(System.currentTimeMillis()));
                    of.setOfferCreated(new Date());
                    of.setOfferDescription(offrDescription.getText().toString());
                    of.setOfferName(offerName.getText().toString());
                    of.setOfferPrice(offerPrice.getText().toString());
                    of.setUrl(taskSnapshot.getDownloadUrl().toString());
                    offers.child(of.getOfferId()).setValue(of);


                    Toast.makeText(AddOffer.this,"Offer Added",Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        }
        else
        {
            of.setOfferId(String.valueOf(System.currentTimeMillis()));
            of.setOfferCreated(new Date());
            of.setOfferDescription(offrDescription.getText().toString());
            of.setOfferName(offerName.getText().toString());
            of.setOfferPrice(offerPrice.getText().toString());
            of.setUrl("");
            offers.child(of.getOfferId()).setValue(of);
            Toast.makeText(AddOffer.this,"Offer Added",Toast.LENGTH_LONG).show();
            finish();
        }
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
            offrImage.setText("Image selected");
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

}
