package com.srisbeauty.srisbeauty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Connect to Sri via phone and email
 */
public class CustomerCare extends AppCompatActivity {

    ImageView call, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

        call = (ImageView) findViewById(R.id.call_care);
        mail = (ImageView) findViewById(R.id.mail_care);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "111111111123445"));
                if (ActivityCompat.checkSelfPermission(CustomerCare.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CustomerCare.this, "Authorize this app to make calls", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(CustomerCare.this, new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else {

                    startActivity(intent);
                }

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "mejablu@gmail.com","jerseytownindia@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Customers Mail");
                email.putExtra(Intent.EXTRA_TEXT, "Sent From Android App\n");
                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

    }
}
