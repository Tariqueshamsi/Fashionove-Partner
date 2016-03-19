package com.fashionove.stvisionary.business.partner.Activity;


import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fashionove.stvisionary.business.partner.R;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    List<String> permissionNeeds = Arrays.asList("email", "user_friends");
    private Map<String, String> params = new HashMap<String, String>();
    private Button facebookLogin;
    private boolean facebook = false;

    private RelativeLayout layoutFacebook;
    private RelativeLayout layoutSms;
    private TextView vendorCompanyName ;

    String vendorName = "";
    String vendorCompany = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences vendorData = PreferenceManager.getDefaultSharedPreferences(this);
        vendorName = vendorData.getString("vendor_name", "");
        vendorCompany = vendorData.getString("vendor_company","");

        if (vendorName.isEmpty() == false) {
            getSupportActionBar().setTitle(vendorName);
        } else {
            getSupportActionBar().setTitle("");
        }


        FacebookSdk.sdkInitialize(getApplicationContext());


        layoutSms = (RelativeLayout) findViewById(R.id.middle_layout);
        layoutFacebook = (RelativeLayout) findViewById(R.id.bottom_layout);
        vendorCompanyName = (TextView)findViewById(R.id.storeNameRow);


        layoutSms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent screen = new Intent(Home.this, ChooseSmsCategory.class);
                startActivity(screen);

            }
        });


        layoutFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                facebook = true;
                doTheFacebookLogin();
                //sharePhotoToFacebook();
            }
        });

        if (vendorCompany.isEmpty() == false)
        {
            vendorCompanyName.setText(vendorCompany);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.logout)
        {

            SharedPreferences vendorData = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = vendorData.edit();
            editor.putBoolean("logged_in",false);

            Intent nextScreen = new Intent(Home.this,Login.class);
            nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(nextScreen);

        }

        return super.onOptionsItemSelected(item);
    }


    public void doTheFacebookLogin() {

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
        LoginManager.getInstance().registerCallback(mCallbackManager, mCallback);
    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();


            sharePhotoToFacebook();


        }

        @Override
        public void onCancel() {
            // customToast("you cancelled");
            Toast.makeText(getApplicationContext(), "Please check your data Connection", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            //   customToast("Oops! Something went wrong, Try again.");
        }

    };

    private void sharePhotoToFacebook() {

        // FacebookSdk.sdkInitialize(getApplicationContext());

        Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(
                "file:///mnt/sdcard/test/v1327056571768.3gpp"));

        startActivity(Intent.createChooser(share, "Share Image"));
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        mCallbackManager.onActivityResult(requestCode, responseCode, data);
    }

}

