package com.fashionove.stvisionary.business.partner.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fashionove.stvisionary.business.partner.Fragment.FragmentSmsTemplate;
import com.fashionove.stvisionary.business.partner.R;

public class ChooseSmsTemplate extends AppCompatActivity {

    String categoryName = "";
    String categoryId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sms_template);

        Intent i = getIntent();
        categoryName = i.getStringExtra("sms_category_name");
        categoryId = i.getStringExtra("sms_category_id");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentSmsTemplate fragment = new FragmentSmsTemplate();
        Bundle bundle = new Bundle();
        bundle.putString("sms_category_id", categoryId);
        // set Fragmentclass Arguments
        fragment.setArguments(bundle);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.smsTemplateLayout, fragment, "sms_template");
        transaction.commit();

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
