package com.fashionove.stvisionary.business.fashionovepartner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    List<String> permissionNeeds = Arrays.asList("email", "user_friends");
    private Map<String, String> params = new HashMap<String, String>();
    private Button facebookLogin;
    private boolean facebook = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_main);


        Button upgrade = (Button) findViewById(R.id.upgrade_button);

        upgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSoftUpdateDialog();
            }
        });


        Button sms = (Button) findViewById(R.id.sms_button);
        sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(MainActivity.this, ChooseTemplate.class);

                // currentContext.startActivity(activityChangeIntent);

                MainActivity.this.startActivity(activityChangeIntent);
            }
        });

        facebookLogin = (Button) findViewById(R.id.fb_button);
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                facebook = true;
                doTheFacebookLogin();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doTheFacebookLogin() {

        FacebookSdk.sdkInitialize(getApplicationContext());
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


    //soft update of the app
    public void showSoftUpdateDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder1.setTitle("Update");
        builder1.setMessage("A new version is available. Please update the App.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("UPDATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //take to the play store page
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    }
                });
        builder1.setNegativeButton("LATER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

