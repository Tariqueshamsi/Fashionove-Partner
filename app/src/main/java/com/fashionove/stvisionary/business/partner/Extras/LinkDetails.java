package com.fashionove.stvisionary.business.partner.Extras;

/**
 * Created by developer on 03/02/16.
 */
public interface LinkDetails {

    public interface URL {

        public static final String URL_SERVER = "vendor.fashionove.com";

        public static final String URL_VENDOR_LOGIN = "http://" +URL_SERVER+ "/vendor/authenticate";
        public static final String URL_AUTHENTICATED_VENDOR = "http://" +URL_SERVER+ "/api/v1/vendor/authenticated_user";

        public static final String URL_SMS_CATEGORY = "http://" +URL_SERVER+ "/api/v1/category";
        public static final String URL_SMS_CATEGORY_PHOTOS = "http://" +URL_SERVER+ "/fashionove_template/public/uploads/category/";
        public static final String URL_SMS_TEMPLATES = "http://" +URL_SERVER+ "/api/v1/templates/";
        public static final String URL_SEND_SMS = "http://vendor.fashionove.com/api/v1/sms";



    }
}
