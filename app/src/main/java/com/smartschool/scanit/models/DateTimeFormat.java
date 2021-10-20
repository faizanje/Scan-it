package com.smartschool.scanit.models;

import android.util.Log;

import com.smartschool.scanit.Constants;

public class DateTimeFormat {
    String dateSeparator = "-";
    int dateFormatIndex = 0;
    String timeFormat = "12 hour";

    public int getDateFormatIndex() {
        return dateFormatIndex;
    }

    public void setDateFormatIndex(int dateFormatIndex) {
        this.dateFormatIndex = dateFormatIndex;
    }

    public String getDateSeparator() {
        return dateSeparator;
    }

    public void setDateSeparator(String dateSeparator) {
        this.dateSeparator = dateSeparator;
    }

    public String getActualDateFormat() {
        String actualDateFormat = "";
        actualDateFormat = Constants.dateFormats[dateFormatIndex].replaceAll("__",dateSeparator);
        actualDateFormat = actualDateFormat + " " + Constants.timeFormatsHashMap.get(timeFormat);
        Log.d(Constants.TAG, "getActualDateFormat: " + actualDateFormat);
        return actualDateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }


}
