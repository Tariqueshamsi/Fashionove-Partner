package com.fashionove.stvisionary.business.fashionovepartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;




import com.fashionove.stvisionary.business.fashionovepartner.ContactPickerActivity;
import com.fashionove.stvisionary.business.fashionovepartner.ContactResult;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MultiPicker extends ActionBarActivity {

    private TextView mTvContacts;
    public static final String CONTACT_PICKER_RESULT = "contacts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_picker);
        mTvContacts = (TextView) findViewById(R.id.tvContacts);
    }

    public void showContacts(View view) {
        // Take care of using a random request code.
        startActivityForResult(new Intent(this, ContactPickerActivity.class), 1302);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1302 && RESULT_OK == resultCode) {
            processContacts((ArrayList<ContactResult>)
                    data.getSerializableExtra(ContactPickerActivity.CONTACT_PICKER_RESULT));
        } else if(RESULT_CANCELED == resultCode) {
            if (data != null && data.hasExtra("error")) {
                mTvContacts.setText(data.getStringExtra("error"));
            } else {
                mTvContacts.setText("Contact selection cancelled");
            }
        }
    }

    private void processContacts(ArrayList<ContactResult> contacts) {
        StringBuilder sb = new StringBuilder();
        for(ContactResult contactResult : contacts) {
            sb.append(contactResult.getContactId());
            sb.append(" <");
            for(ContactResult.ResultItem item : contactResult.getResults()) {
                sb.append(item.getResult());
                if(contactResult.getResults().size() > 1) {
                    sb.append(", ");
                }
            }
            sb.append(">, ");
        }
        mTvContacts.setText(sb.toString());
    }
}