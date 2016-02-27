package com.fashionove.stvisionary.business.partner.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fashionove.stvisionary.business.partner.Activity.SetupSms;
import com.fashionove.stvisionary.business.partner.Adapter.AdapterPhoneContact;

import com.fashionove.stvisionary.business.partner.GetterSetter.ContactData;
import com.fashionove.stvisionary.business.partner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChooseContact extends Fragment {

    private ArrayList<ContactData> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterPhoneContact adapter;
    private LinearLayoutManager manager;

    private ActivityCommunicator activityCommunicator;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    public FragmentChooseContact() {
        // Required empty public constructor
    }

    public interface ActivityCommunicator {
        public void passDataToActivity(ArrayList<ContactData> contactList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activityCommunicator = (ActivityCommunicator) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityCommunicator");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_contact, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done_item) {

            //handle the click event
            checkedTheCheckedItem();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.done_item).setVisible(true);
        menu.findItem(R.id.done_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.contactList);
        adapter = new AdapterPhoneContact(getActivity());
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        checkPermissionForContact();


    }

    public void checkPermissionForContact() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            readContactFromPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                readContactFromPhone();
            } else {

                getActivity().getFragmentManager().beginTransaction().remove(this).commit();

                Toast.makeText(getActivity(), "We need your permission to display contact.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readContactFromPhone() {
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactData contactData = new ContactData();
            contactData.setName(name);
            contactData.setNumber(phoneNumber);
            contactData.setIsSelected(false);
            contactData.setViewType(1);
            contactList.add(contactData);
            Log.i("Name : ", name);
            Log.i("Number : ", phoneNumber);

        }
        phones.close();

        adapter.setPhoneContactData(contactList);
    }

    public void checkedTheCheckedItem() {
        ArrayList<ContactData> listContact = new ArrayList<>();

        for (int i = 0; i < contactList.size(); i++) {
            ContactData contactData = contactList.get(i);
            if (contactData.getIsSelected() == true) {
                // Log.i("Checked : "+contactData.getName() ,contactData.getNumber());
                listContact.add(contactData);
            }
        }

        Toast.makeText(getActivity(), "Size = " + listContact.size(), Toast.LENGTH_SHORT).show();
        if (listContact != null && listContact.size() > 0 && activityCommunicator != null) {
            //send the data to the activity class
            activityCommunicator.passDataToActivity(listContact);

            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        } else {
            Toast.makeText(getActivity(), "Please select contacts.", Toast.LENGTH_SHORT).show();
        }


    }


}
