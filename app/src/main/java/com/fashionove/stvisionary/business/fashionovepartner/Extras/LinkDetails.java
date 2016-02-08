package com.fashionove.stvisionary.business.fashionovepartner.Extras;

/**
 * Created by developer on 03/02/16.
 */
public interface LinkDetails {

    public interface URL {

        public static final String URL_SERVER = "192.168.1.14";

        public static final String URL_VENDOR_LOGIN = "http://" +URL_SERVER+ "/fashionove_template/public/api/v1/vendor/authenticate";

        public static final String URL_SMS_CATEGORY = "http://" +URL_SERVER+ "/fashionove_template/public/api/v1/category";
        public static final String URL_SMS_CATEGORY_PHOTOS = "http://" +URL_SERVER+ "/fashionove_template/public/uploads/category/";
        public static final String URL_SMS_TEMPLATES = "http://" +URL_SERVER+ "/fashionove_template/public/api/v1/templates/";



    }
}
