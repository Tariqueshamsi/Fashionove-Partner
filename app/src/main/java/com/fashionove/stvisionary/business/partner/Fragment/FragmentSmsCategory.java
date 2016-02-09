package com.fashionove.stvisionary.business.partner.Fragment;


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
import com.fashionove.stvisionary.business.partner.Activity.ChooseSmsTemplate;
import com.fashionove.stvisionary.business.partner.Adapter.AdapterSmsCategory;
import com.fashionove.stvisionary.business.partner.GetterSetter.SmsCategoryData;
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
public class FragmentSmsCategory extends Fragment {


    private RecyclerView recyclerView;
    private AdapterSmsCategory adapter;
    private LinearLayoutManager manager;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    ArrayList<SmsCategoryData> list = new ArrayList<>();
    public static final int ViewTypeCategory = 1;

    public FragmentSmsCategory() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms_category, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.categoryList);
        adapter = new AdapterSmsCategory(getActivity());
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceItemDecorator(16));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //handle the touch event
                SmsCategoryData data;

                switch (list.get(position).getViewType())
                {
                    case ViewTypeCategory:

                        data = list.get(position);
                        Intent intent = new Intent(getActivity(), ChooseSmsTemplate.class);
                        intent.putExtra("sms_category_id", data.getCategoryId());
                        intent.putExtra("sms_category_name",data.getCategoryName());
                        startActivity(intent);

                        break;
                }

            }
        }));

        //call server to fetch data for sms category
        callServer(getUrl());

    }

    public String getUrl()
    {
        String URL = "";

        URL = LinkDetails.URL.URL_SMS_CATEGORY;

        return URL;
    }

    public void callServer(String url)
    {
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
                        adapter.setSmsCategoryData(list);

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                Log.i("Error",error.toString());
                if (error instanceof NoConnectionError || error instanceof TimeoutError) {

                }


            }
        });

        requestQueue.add(request);

    }

    public void parseJsonData(JSONObject response)
    {

        if(response != null && response.length() > 0)
        {
            try {
                JSONArray jsonArray = response.getJSONArray(Keys.Endpoint.KEY_DATA);

                for (int i=0;i<jsonArray.length();i++)
                {
                    String categoryName = "";
                    String categoryImage = "";
                    String categoryId = "";

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if(jsonObject.has(Keys.Endpoint.KEY_SMS_CATEGORY_ID) && !jsonObject.isNull(Keys.Endpoint.KEY_SMS_CATEGORY_ID))
                    {
                        categoryId = jsonObject.getString(Keys.Endpoint.KEY_SMS_CATEGORY_ID);
                    }

                    if(jsonObject.has(Keys.Endpoint.KEY_SMS_CATEGORY_NAME) && !jsonObject.isNull(Keys.Endpoint.KEY_SMS_CATEGORY_NAME))
                    {
                        categoryName = jsonObject.getString(Keys.Endpoint.KEY_SMS_CATEGORY_NAME);
                    }

                    if(jsonObject.has(Keys.Endpoint.KEY_SMS_CATEGORY_IMAGE) && !jsonObject.isNull(Keys.Endpoint.KEY_SMS_CATEGORY_IMAGE))
                    {
                        categoryImage = LinkDetails.URL.URL_SMS_CATEGORY_PHOTOS.trim() + jsonObject.getString(Keys.Endpoint.KEY_SMS_CATEGORY_IMAGE).trim();
                    }

                    SmsCategoryData smsCategoryData = new SmsCategoryData();
                    smsCategoryData.setCategoryId(categoryId);
                    smsCategoryData.setCategoryName(categoryName);
                    smsCategoryData.setCategoryImage(categoryImage);
                    smsCategoryData.setViewType(1);

                    list.add(smsCategoryData);

                }


            }catch (JSONException e)
            {

            }


        }

    }
}
