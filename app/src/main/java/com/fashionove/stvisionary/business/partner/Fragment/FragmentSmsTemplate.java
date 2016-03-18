package com.fashionove.stvisionary.business.partner.Fragment;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashionove.stvisionary.business.partner.Activity.Login;
import com.fashionove.stvisionary.business.partner.Activity.SetupSms;
import com.fashionove.stvisionary.business.partner.Adapter.AdapterSmsTemplate;
import com.fashionove.stvisionary.business.partner.GetterSetter.SmsTemplateData;
import com.fashionove.stvisionary.business.partner.Interface.ClickListener;
import com.fashionove.stvisionary.business.partner.Network.HttpsTrustManager;
import com.fashionove.stvisionary.business.partner.Network.VolleySingleton;
import com.fashionove.stvisionary.business.partner.R;
import com.fashionove.stvisionary.business.partner.Utilities.RecyclerTouchListener;
import com.fashionove.stvisionary.business.partner.Utilities.SpaceItemDecorator;
import com.fashionove.stvisionary.business.partner.Extras.Keys;
import com.fashionove.stvisionary.business.partner.Extras.LinkDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSmsTemplate extends Fragment {

    String categoryId = "";

    private RecyclerView recyclerView;
    private AdapterSmsTemplate adapter;
    private LinearLayoutManager manager;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    ArrayList<SmsTemplateData> list = new ArrayList<>();
    public static final int ViewTypeCategory = 1;


    public FragmentSmsTemplate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        categoryId = getArguments().getString("sms_category_id");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms_template, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.templateList);
        adapter = new AdapterSmsTemplate(getActivity());
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceItemDecorator(16));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //handle the touch event
                SmsTemplateData data;

                switch (list.get(position).getViewType()) {
                    case ViewTypeCategory:
                        data = list.get(position);

                        Intent screen = new Intent(getActivity(), SetupSms.class);
                        screen.putExtra("sms_template_name",data.getTemplateName());
                        startActivity(screen);

                        break;
                }

            }
        }));

        //call server to fetch data for sms category
        callServer(getUrl());


    }

    public String getUrl() {
        String URL = "";
        String accessToken = "";

        SharedPreferences loginCredential = PreferenceManager.getDefaultSharedPreferences(getActivity());
        accessToken = loginCredential.getString("access_token","");


        URL = LinkDetails.URL.URL_SMS_TEMPLATES + categoryId + "?token=" + accessToken;

        return URL.trim();
    }

    public void callServer(String url) {
        HttpsTrustManager.allowAllSSL();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //parse the JSON data
                        parseJsonData(response);

                        //pass the data to adapter class
                        adapter.setSmsTemplateData(list);

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Please check your data connection", Toast.LENGTH_SHORT).show();
                }

                if(error != null) {
                    if (error instanceof ServerError ) {
                        SharedPreferences vendorData = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = vendorData.edit();
                        editor.putBoolean("logged_in", false);
                        Toast.makeText(getActivity(), "Token expired,please login", Toast.LENGTH_SHORT).show();
                        Intent nextScreen = new Intent(getActivity(), Login.class);
                        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(nextScreen);
                    }
                }


            }
        });

        requestQueue.add(request);
    }

    public void parseJsonData(JSONObject response) {
        if (response != null && response.length() > 0) {

            try {

                JSONArray jsonArray = response.getJSONArray(Keys.Endpoint.KEY_DATA);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String templateId = "";
                    String templateName = "";

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.has(Keys.Endpoint.KEY_SMS_TEMPLATE_ID) && !jsonObject.isNull(Keys.Endpoint.KEY_SMS_TEMPLATE_ID)) {
                        templateId = jsonObject.getString(Keys.Endpoint.KEY_SMS_TEMPLATE_ID);
                    }

                    if (jsonObject.has(Keys.Endpoint.KEY_SMS_TEMPLATE_NAME) && !jsonObject.isNull(Keys.Endpoint.KEY_SMS_TEMPLATE_NAME)) {
                        templateName = jsonObject.getString(Keys.Endpoint.KEY_SMS_TEMPLATE_NAME);

                        templateName = getConvertedTemplate(templateName);
                    }


                    SmsTemplateData smsTemplateData = new SmsTemplateData();
                    smsTemplateData.setTemplateId(templateId);
                    smsTemplateData.setTemplateName(templateName);
                    smsTemplateData.setViewType(1);

                    list.add(smsTemplateData);

                }

            } catch (JSONException e) {

            }
        }

    }

    public String getConvertedTemplate(String template)
    {
        int i = 0;
        String s = "";
        String vendorCompany = "";
        char p = ' ',q =' ';
        SharedPreferences loginCredential = PreferenceManager.getDefaultSharedPreferences(getActivity());
        vendorCompany = loginCredential.getString("vendor_company","");

        template.trim();
        while (i < template.length()-1)
        {
            p= template.charAt(i);
            q=template.charAt(i+1);

            if(p == '$' && q == '$')
            {
                s = s + vendorCompany +" " ;
                i = i+2;
            }else {
                s = s + p;
                i++;
            }

        }

        return s;
    }

}
