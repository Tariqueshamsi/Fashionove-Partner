package com.fashionove.stvisionary.business.partner.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fashionove.stvisionary.business.partner.Network.VolleySingleton;
import com.fashionove.stvisionary.business.partner.R;
import com.fashionove.stvisionary.business.partner.Extras.Keys;
import com.fashionove.stvisionary.business.partner.Extras.LinkDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    Map<String, String> params = new HashMap<String, String>();

    private CoordinatorLayout coordinatorLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private EditText emailIdText;
    private EditText passwordText;
    private Button loginButton;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();


        emailLayout = (TextInputLayout) findViewById(R.id.input_layout_email);
        passwordLayout = (TextInputLayout) findViewById(R.id.input_layout_password);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        emailIdText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check th validations
                checkValidation();

            }
        });


        passwordText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (passwordText.isFocused()) {
                        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                    //check th validations
                    checkValidation();

                    return true;
                }
                return false;
            }
        });


    }

    public void checkValidation() {
        email = emailIdText.getText().toString().trim();
        password = passwordText.getText().toString();

        //check for blank
        if (checkForBlank() == false) {

            if (checkForPatternValidation() == true) {

                //call the server to authenticate
                callServerForLoginAccess();
                //Toast.makeText(this, "Now call server", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public boolean checkForBlank() {
        int e = 0;
        int p = 0;
        boolean flag = false;

        if (email.isEmpty() == true) {
            flag = true;
            e = 1;
        }

        if (password.isEmpty() == true) {
            flag = true;
            p = 1;
        }

        if (e == 1 && p == 1) {
            emailLayout.setErrorEnabled(true);
            passwordLayout.setErrorEnabled(true);

            emailLayout.setError("Email Id is blank");
            passwordLayout.setError("Password is blank");

        } else if (e == 1 && p == 0) {
            emailLayout.setError("Email Id is blank");
            emailLayout.setErrorEnabled(true);

            // passwordLayout.setError(null);
            passwordLayout.setErrorEnabled(false);
        } else if (p == 1 && e == 0) {
            passwordLayout.setError("Password is blank");
            passwordLayout.setErrorEnabled(true);

            emailLayout.setErrorEnabled(false);
        } else if (e == 0 && p == 0) {

            emailLayout.setErrorEnabled(false);
            passwordLayout.setErrorEnabled(false);


        }


        return flag;
    }

    public boolean checkForPatternValidation() {
        boolean flag = true;

        if (checkEmailValidation() == false) {

            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Invalid email id");
            flag = false;
        } else {
            flag = true;
            emailLayout.setErrorEnabled(false);

        }


        return flag;
    }

    public boolean checkEmailValidation() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    //Calling Server with Post Request
    public void callServerForLoginAccess() {


        StringRequest request = new StringRequest(Request.Method.POST,
                LinkDetails.URL.URL_VENDOR_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       
                        emailLayout.setErrorEnabled(false);
                        passwordLayout.setErrorEnabled(false);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String accessToken = "";
                            accessToken = parseJsonDataForAccessToken(jsonObject);

                            if (!accessToken.isEmpty()) {
                                //save token in local
                                SharedPreferences loginCredential = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                SharedPreferences.Editor editor = loginCredential.edit();
                                editor.putString("access_token", accessToken);
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Access Token = " + accessToken, Toast.LENGTH_SHORT).show();
                                //server request to get the client details
                                 callServerForVendorData(getUrl(accessToken));



                            } else {
                                //error
                                showSnackBar("Something went wrong please try again later.");
                            }

                        } catch (Throwable t) {

                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //invalid user credentials
                if (error instanceof AuthFailureError) {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(" ");

                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(" ");

                    showSnackBar("Invalid login credential.");
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Toast.makeText(getApplicationContext(),"Invalid user Id", Toast.LENGTH_LONG).show();
                    showSnackBar("Please check your data connection.");
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                params.put("email", email);
                params.put("password", password);

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
        };
        requestQueue.add(request);
    }

    public void showSnackBar(String s) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public String parseJsonDataForAccessToken(JSONObject response) {
        String accessToken = "";

        if (response != null && response.length() > 0) {
            try {
                    if (response.has(Keys.Endpoint.KEY_ACCESS_TOKEN) && !response.isNull(Keys.Endpoint.KEY_ACCESS_TOKEN)) {
                        accessToken = response.getString(Keys.Endpoint.KEY_ACCESS_TOKEN);

                    }

            } catch (JSONException e) {

            }
        }

        return accessToken;

    }

    public String getUrl(String accessToken)
    {
        String URL = "";

            URL = LinkDetails.URL.URL_AUTHENTICATED_VENDOR + "?token=" + accessToken;

        return URL;
    }

    public void callServerForVendorData(String url)
    {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //parse outlet json and store in local
                        if (parseJsonDataForOutletData(response) == true) {
                            //login successfull
                            //open new intent
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Toast.makeText(getApplicationContext(),"Invalid user Id", Toast.LENGTH_LONG).show();
                    showSnackBar("Please check your data connection.");
                }
            }
        });

        requestQueue.add(request);

    }

    public boolean parseJsonDataForOutletData(JSONObject response)
    {
        String vendorId = "";
        String vendorName = "";
        String vendorEmail = "";
        String vendorCompany = "";
        String vendorPhone = "";
        boolean flag = false;

        if(response != null && response.length() > 0)
        {
            try {

                if(response.has(Keys.Endpoint.KEY_DATA) && !response.isNull(Keys.Endpoint.KEY_DATA))
                {
                    flag = true;
                    JSONObject jsonObject = response.getJSONObject(Keys.Endpoint.KEY_DATA);

                    // vendor id
                    if(jsonObject.has(Keys.Endpoint.KEY_VENDOR_ID) && !jsonObject.isNull(Keys.Endpoint.KEY_VENDOR_ID))
                    {
                        vendorId = jsonObject.getString(Keys.Endpoint.KEY_VENDOR_ID);
                    }

                    //ventor name
                    if(jsonObject.has(Keys.Endpoint.KEY_VENDOR_NAME) && !jsonObject.isNull(Keys.Endpoint.KEY_VENDOR_NAME))
                    {
                        vendorName = jsonObject.getString(Keys.Endpoint.KEY_VENDOR_NAME);
                    }

                    //vendor email
                    if(jsonObject.has(Keys.Endpoint.KEY_VENDOR_EMAIL) && !jsonObject.isNull(Keys.Endpoint.KEY_VENDOR_EMAIL))
                    {
                        vendorEmail = jsonObject.getString(Keys.Endpoint.KEY_VENDOR_EMAIL);
                    }

                    //vendor company
                    if(jsonObject.has(Keys.Endpoint.KEY_VENDOR_COMPANY) && !jsonObject.isNull(Keys.Endpoint.KEY_VENDOR_COMPANY))
                    {
                        vendorCompany = jsonObject.getString(Keys.Endpoint.KEY_VENDOR_COMPANY);
                    }

                    //vendor phone
                    if(jsonObject.has(Keys.Endpoint.KEY_VENDOR_PHONE) && !jsonObject.isNull(Keys.Endpoint.KEY_VENDOR_PHONE))
                    {
                        vendorPhone = jsonObject.getString(Keys.Endpoint.KEY_VENDOR_PHONE);
                    }

                    //save as local data
                    SharedPreferences vendorData = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor editor = vendorData.edit();
                    editor.putString("vendor_id",vendorId);
                    editor.putString("vendor_name",vendorName);
                    editor.putString("vendor_email",vendorEmail);
                    editor.putString("vendor_company",vendorCompany);
                    editor.putString("vendor_phone",vendorPhone);
                    editor.apply();




                }


            }catch (JSONException e){

            }
        }

        return flag;

    }


}
