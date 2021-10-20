package com.smartschool.scanit.shared;

import com.smartschool.scanit.models.DateTimeFormat;
import com.smartschool.scanit.utils.DBUtils;

public class Config {
    private static Config config;
    private boolean isContinuousScanning, isAlarmRingtone;
    private DateTimeFormat dateTimeFormat;
    private String storagePath = "";

    public Config(){
        isContinuousScanning = DBUtils.readIsContinuousScanning();
        isAlarmRingtone = DBUtils.readIsAlarmSound();
        dateTimeFormat = DBUtils.readDateTimeFormat();
    }

    public static void init() {
        if (config == null) {
            config = new Config();
        }
    }

    public DateTimeFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(DateTimeFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
        DBUtils.saveDateTimeFormat(dateTimeFormat);
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public static Config getInstance() {
        if (config == null) {
            init();
        }
        return config;
    }

    public boolean isContinuousScanning() {
        return isContinuousScanning;
    }

    public void setContinuousScanning(boolean continuousScanning) {
        isContinuousScanning = continuousScanning;
        DBUtils.readIsContinuousScanning(continuousScanning);
    }

    public boolean isAlarmRingtone() {
        return isAlarmRingtone;
    }

    public void setAlarmRingtone(boolean alarmRingtone) {
        isAlarmRingtone = alarmRingtone;
        DBUtils.saveIsAlarmSound(isAlarmRingtone);
    }
}
