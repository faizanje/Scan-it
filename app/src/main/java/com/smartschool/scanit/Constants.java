package com.smartschool.scanit;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {

    public static final String TAG = "shobbit";
    /* Book Constant */
    public static final String BOOK_RECORDS = "BOOK_RECORDS";
    public static final String BOOK_CONFIG = "BOOK_CONFIG";

    /* Key constants */
    public static final String KEY_RECORD_TYPE = "KEY_RECORD_TYPE";
    public static final String KEY_STORAGE_PATH = "KEY_STORAGE_PATH";
    public static final String KEY_IS_ALARM_SOUND = "KEY_IS_ALARM_SOUND";
    public static final String KEY_IS_CONTINUOUS_SCANNING = "KEY_IS_CONTINUOUS_SCANNING";
    public static final String KEY_DATE_TIME_FORMAT = "KEY_DATE_TIME_FORMAT";

//    public static final String[] dateFormats = new String[]{
//            "MM__DD__YYYY",
//            "DD__MM__YYYY",
//            "YYYY__MM__DD"};

    public static final String[] dateFormats = new String[]{
            "MM__dd__yyyy",
            "dd__MM__yyyy",
            "yyyy__MM__dd"};
//            put("Month__Day Year", "MM__DD__YYYY");
//            put("Day__Month__Year", "DD__MM__YYYY");
//            put("Year__Month__Day", "YYYY__MM__DD");

    public static final String[] timeFormats = new String[]{"HH:MM:SS XM", "HH:MM:SS"};
    public static HashMap<String, String> timeFormatsHashMap = new HashMap<String, String>() {
        {
            put("12 hour", "HH:mm:ss a");
            put("24 hour", "HH:mm:ss");
        }
    };
}
