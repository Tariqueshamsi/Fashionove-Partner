package com.fashionove.stvisionary.business.partner.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fashionove.stvisionary.business.partner.Adapter.AdapterPhoneContact;
import com.fashionove.stvisionary.business.partner.GetterSetter.ContactData;
import com.fashionove.stvisionary.business.partner.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChooseContact extends Fragment {

    private ArrayList<ContactData> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterPhoneContact adapter;
    private LinearLayoutManager manager;

    private static final int INVALID_POSITION = -1;
    private int mActivatedPosition = INVALID_POSITION;
    private AdapterPhoneContact mAdapter;
    private ActionMode mActionMode;


    public FragmentChooseContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_contact, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.contactList);
        adapter = new AdapterPhoneContact(getActivity());
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        readContactFromPhone();


    }

    public void readContactFromPhone()
    {
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactData contactData = new ContactData();
            contactData.setName(name);
            contactData.setNumber(phoneNumber);
            contactData.setViewType(1);
            contactList.add(contactData);
            Log.i("Name : ", name);
            Log.i("Number : ",phoneNumber);

        }
        phones.close();

        adapter.setPhoneContactData(contactList);
    }


}
