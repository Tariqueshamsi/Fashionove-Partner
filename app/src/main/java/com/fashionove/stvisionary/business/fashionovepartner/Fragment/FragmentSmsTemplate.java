package com.fashionove.stvisionary.business.fashionovepartner.Fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashionove.stvisionary.business.fashionovepartner.Activity.SetupSms;
import com.fashionove.stvisionary.business.fashionovepartner.Adapter.AdapterSmsTemplate;
import com.fashionove.stvisionary.business.fashionovepartner.GetterSetter.SmsTemplateData;
import com.fashionove.stvisionary.business.fashionovepartner.Interface.ClickListener;
import com.fashionove.stvisionary.business.fashionovepartner.Network.HttpsTrustManager;
import com.fashionove.stvisionary.business.fashionovepartner.Network.VolleySingleton;
import com.fashionove.stvisionary.business.fashionovepartner.R;
import com.fashionove.stvisionary.business.fashionovepartner.Utilities.RecyclerTouchListener;
import com.fashionove.stvisionary.business.fashionovepartner.Utilities.SpaceItemDecorator;

import static com.fashionove.stvisionary.business.fashionovepartner.Extras.Keys.Endpoint.KEY_SMS_TEMPLATE_ID;
import static com.fashionove.stvisionary.business.fashionovepartner.Extras.Keys.Endpoint.KEY_SMS_TEMPLATE_NAME;
import static com.fashionove.stvisionary.business.fashionovepartner.Extras.LinkDetails.URL.URL_SMS_TEMPLATES;
import static com.fashionove.stvisionary.business.fashionovepartner.Extras.Keys.Endpoint.KEY_DATA;

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

        URL = URL_SMS_TEMPLATES + categoryId;

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
                    Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

        requestQueue.add(request);
    }

    public void parseJsonData(JSONObject response) {
        if (response != null && response.length() > 0) {

            try {

                JSONArray jsonArray = response.getJSONArray(KEY_DATA);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String templateId = "";
                    String templateName = "";

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.has(KEY_SMS_TEMPLATE_ID) && !jsonObject.isNull(KEY_SMS_TEMPLATE_ID)) {
                        templateId = jsonObject.getString(KEY_SMS_TEMPLATE_ID);
                    }

                    if (jsonObject.has(KEY_SMS_TEMPLATE_NAME) && !jsonObject.isNull(KEY_SMS_TEMPLATE_NAME)) {
                        templateName = jsonObject.getString(KEY_SMS_TEMPLATE_NAME);
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

}
