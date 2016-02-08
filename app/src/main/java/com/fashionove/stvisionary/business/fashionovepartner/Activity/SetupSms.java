package com.fashionove.stvisionary.business.fashionovepartner.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.fashionove.stvisionary.business.fashionovepartner.R;

public class SetupSms extends AppCompatActivity {

    String templateName = "";
    private RelativeLayout addContactLayout;
    private RelativeLayout sendSmsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_sms);


        Intent i = getIntent();
        templateName = i.getStringExtra("sms_template_name");

        getSupportActionBar().setTitle("Setup Sms");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addContactLayout = (RelativeLayout)findViewById(R.id.addContactNumber);
        sendSmsLayout = (RelativeLayout)findViewById(R.id.sendSms);

        addContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open contact choose layout
            }
        });

        sendSmsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send sms to all the contact choosen
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
