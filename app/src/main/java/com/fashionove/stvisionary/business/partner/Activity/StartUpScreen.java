package com.fashionove.stvisionary.business.partner.Activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fashionove.stvisionary.business.partner.R;


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
            Intent intent = new Intent(this, Home.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            Intent nextScreen = new Intent(StartUpScreen.this,Home.class);
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

}
