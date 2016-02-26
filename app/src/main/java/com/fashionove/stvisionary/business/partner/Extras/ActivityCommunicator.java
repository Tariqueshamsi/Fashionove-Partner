package com.fashionove.stvisionary.business.partner.Extras;

import com.fashionove.stvisionary.business.partner.GetterSetter.ContactData;

import java.util.ArrayList;

/**
 * Created by developer on 26/02/16.
 */
public interface ActivityCommunicator {
    public void passDataToActivity(ArrayList<ContactData> contactList);
}
