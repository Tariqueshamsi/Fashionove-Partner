package com.fashionove.stvisionary.business.fashionovepartner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddContact extends AppCompatActivity {

    private static final int PICK_CONTACT = 1;
    private TextView text;
    private TextView textContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        textContact = (TextView) findViewById(R.id.text_contact);
        TextView text = (TextView) findViewById(R.id.choose_contact);
        //  TextView text = (TextView) findViewById(R.id.text_contact);
  /*      textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               Intent intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });  */
    /*    text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

              //  sendSMS();

                Intent activityChangeIntent = new Intent(AddContact.this,MultiPicker.class);

                // currentContext.startActivity(activityChangeIntent);

                AddContact.this.startActivity(activityChangeIntent);

            }
        });  */

        Button upgrade = (Button) findViewById(R.id.upgrade_button);

        upgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSoftUpdateDialog();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
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

    protected void sendSMS() {

        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);


        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", " ");
        //    smsIntent.putExtra("sms_body", " ");


        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddContact.this,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showContact(View view) {
        // Take care of using a random request code.
        startActivityForResult(new Intent(this, ContactPickerActivity.class), 1302);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1302 && RESULT_OK == resultCode) {
            processContacts((ArrayList<ContactResult>)
                    data.getSerializableExtra(ContactPickerActivity.CONTACT_PICKER_RESULT));
        } else if (RESULT_CANCELED == resultCode) {
            if (data != null && data.hasExtra("error")) {
                textContact.setText(data.getStringExtra("error"));
            } else {
                textContact.setText("Contact selection cancelled");
            }
        }
    }

    private void processContacts(ArrayList<ContactResult> contacts) {
        StringBuilder sb = new StringBuilder();
        for (ContactResult contactResult : contacts) {
            //   sb.append(contactResult.getContactId());
            sb.append(" <");
            for (ContactResult.ResultItem item : contactResult.getResults()) {
                sb.append(item.getResult());
                if (contactResult.getResults().size() > 1) {
                    sb.append(", ");
                }
            }
            sb.append(">, ");
        }
        textContact.setText(sb.toString());
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
