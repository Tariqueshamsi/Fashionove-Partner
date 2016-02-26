package com.fashionove.stvisionary.business.partner.GetterSetter;

/**
 * Created by developer on 09/02/16.
 */
public class ContactData {

    private String name = "";
    private String number = "";
    private boolean isSelected = false;
    private int ViewType = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }
}
