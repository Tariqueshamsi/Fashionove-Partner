package com.fashionove.stvisionary.business.partner.Applications;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by developer on 01/02/16.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Fresco.initialize(this);

        printHashKey();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {

        return sInstance.getApplicationContext();
    }

    public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fashionove.stvisionary.business.fashionovepartner",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Fashionove hash Key:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}
