package com.fashionove.stvisionary.business.partner.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fashionove.stvisionary.business.partner.Fragment.FragmentChooseContact;
import com.fashionove.stvisionary.business.partner.GetterSetter.ContactData;
import com.fashionove.stvisionary.business.partner.R;

import java.util.ArrayList;
import java.util.List;

public class SetupSms extends AppCompatActivity implements FragmentChooseContact.ActivityCommunicator {

    String templateName = "";
    private RelativeLayout addContactLayout;
    private RelativeLayout sendSmsLayout;

    private TextView numberContactChoosen;
    private FragmentManager manager;
    int pop = 0;

    ArrayList<ContactData> listContact = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_sms);

        getSupportActionBar().setTitle("Sms Setup");
        Intent i = getIntent();
        templateName = i.getStringExtra("sms_template_name");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addContactLayout = (RelativeLayout) findViewById(R.id.addContactNumber);
        sendSmsLayout = (RelativeLayout) findViewById(R.id.sendSms);
        numberContactChoosen = (TextView) findViewById(R.id.numberContactChoosen);

        addContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open ContactData choose layout
                openContactFragmnet();
            }
        });

        sendSmsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send sms to all the ContactData choosen
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup_sms, menu);

        MenuItem itemDone = menu.findItem(R.id.done_item);
        itemDone.setVisible(false);

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


            FragmentChooseContact fragment = (FragmentChooseContact) getFragmentManager().findFragmentByTag("fragment_contact");
            if (fragment != null && fragment.isVisible() == true) {

                getSupportActionBar().setTitle("Sms Setup");
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(fragment);
                manager.popBackStackImmediate();
                transaction.commit();


            } else {
                finish();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void openContactFragmnet() {
        FragmentChooseContact fragment = new FragmentChooseContact();

        getSupportActionBar().setTitle("Choose Contact");
        manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layoutContactFragment, fragment, "fragment_contact");
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {


        FragmentChooseContact fragment = (FragmentChooseContact) getFragmentManager().findFragmentByTag("fragment_contact");
        if (fragment != null && fragment.isVisible() == true) {

            getSupportActionBar().setTitle("Sms Setup");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fragment);
            manager.popBackStackImmediate();
            transaction.commit();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void passDataToActivity(ArrayList<ContactData> contactList) {
        //now send the data to the server
        // Toast.makeText(this,"Chosen Number = "+contactList.size(),Toast.LENGTH_SHORT).show();
        this.listContact = contactList;
        if (listContact != null) {
            numberContactChoosen.setText(String.valueOf(listContact.size()));
        }

    }
}
