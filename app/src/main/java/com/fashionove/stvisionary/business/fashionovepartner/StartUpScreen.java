package com.fashionove.stvisionary.business.fashionovepartner;

import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class StartUpScreen extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 2000;
  //  boolean logedStatus = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);




        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {
                    //call function
                    openIntent();
                    finish();
                }
            }


        };
        splashTread.start();


    }

    public void openIntent() {
        if (checkForAlreadyLogined()) {
            Intent intent = new Intent(this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            Intent nextScreen = new Intent(StartUpScreen.this,MainActivity.class);

            //nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(nextScreen);

        }

    }

    public Boolean checkForAlreadyLogined() {

        boolean flag = false;
        SharedPreferences loginData = PreferenceManager.getDefaultSharedPreferences(this);
        if (loginData.getBoolean("logged_in", false) ){
            flag = true;
        }

        return flag;

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_up_screen, menu);
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
}
