package com.fashionove.stvisionary.business.fashionovepartner.GetterSetter;

/**
 * Created by developer on 03/02/16.
 */
public class SmsTemplateData {

    private String templateId = "";
    private String templateName = "";
    private int ViewType = 0;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }
}
