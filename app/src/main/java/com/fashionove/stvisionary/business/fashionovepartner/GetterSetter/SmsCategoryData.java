package com.fashionove.stvisionary.business.fashionovepartner.GetterSetter;

/**
 * Created by developer on 03/02/16.
 */
public class SmsCategoryData {

    private String categoryImage = "";
    private String categoryName = "";
    private String categoryId = "";
    private int viewType = 0;

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
