package com.fashionove.stvisionary.business.partner.Activity;

import android.accounts.NetworkErrorException;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fashionove.stvisionary.business.partner.Fragment.FragmentChooseContact;
import com.fashionove.stvisionary.business.partner.GetterSetter.ContactData;
import com.fashionove.stvisionary.business.partner.Network.VolleySingleton;
import com.fashionove.stvisionary.business.partner.R;

import static com.fashionove.stvisionary.business.partner.Extras.LinkDetails.URL.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetupSms extends AppCompatActivity implements FragmentChooseContact.ActivityCommunicator {

    String templateName = "";
    private RelativeLayout addContactLayout;
    private RelativeLayout sendSmsLayout;

    private TextView numberContactChoosen;
    private TextView smsDetails;
    private FragmentManager manager;
    String numberText = "";
    int mStatusCode = 0;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    Map<String, String> params = new HashMap<String, String>();

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

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();


        addContactLayout = (RelativeLayout) findViewById(R.id.addContactNumber);
        sendSmsLayout = (RelativeLayout) findViewById(R.id.sendSms);
        numberContactChoosen = (TextView) findViewById(R.id.numberContactChoosen);
        smsDetails = (TextView) findViewById(R.id.smsDetail);

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
                sendSmsDetailsToServer(getUrl());
            }
        });

        if (templateName.isEmpty() == false) {
            smsDetails.setText(templateName);
        }

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
            getNumber(contactList);
        }

    }

    public void getNumber(ArrayList<ContactData> contactList) {
        String s = "";

        for (int i = 0; i < contactList.size(); i++) {
            s = s + filteredNumber(contactList.get(i).getNumber()) + ",";
        }

        if (s.isEmpty() != true) {
            numberText = s;
        }

    }

    public String filteredNumber(String number) {
        String s = "";
        int i = number.length() - 1;
        char p = ' ';

        while (i >= 0) {
            p = number.charAt(i);
            if (p == ' ') {
                i--;
            } else if (s.length() == 10) {
                break;
            } else {
                s = s + p;
                i--;
            }
        }

        return s;
    }

    public String getUrl() {
        String Url = "";
        String accessToken = "";

        SharedPreferences loginCredential = PreferenceManager.getDefaultSharedPreferences(this);
        accessToken = loginCredential.getString("access_token", "");

        if (numberText.isEmpty() == false) {
            if (numberText.charAt(numberText.length() - 1) == ',') {
                numberText = numberText.substring(0, numberText.length() - 1);
            }
        }

        Url = URL_SEND_SMS + "?token=" + accessToken;


        return Url.trim();
    }

    //call server to send smms details
    public void sendSmsDetailsToServer(String url) {

        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Sms send successfully", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //invalid user credentials
                if (error instanceof AuthFailureError) {
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.i("contacts", numberText);
                Log.i("message", templateName);
                params.put("contacts", numberText);
                params.put("message", templateName);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                return pars;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected void deliverResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Delivery = " + response, Toast.LENGTH_LONG).show();
                super.deliverResponse(response);
            }
        };
        requestQueue.add(request);

    }
}
