package com.smartschool.scanit.shared;

public class Config {
    private static Config config;

    private boolean isContinuousScanning, isAlarmRingtone;

    public void init(){

    }

    public Config getInstance() {
        if(config == null){
            init();
        }
        return config;
    }

    public boolean isContinuousScanning() {
        return isContinuousScanning;
    }

    public void setContinuousScanning(boolean continuousScanning) {
        isContinuousScanning = continuousScanning;
    }

    public boolean isAlarmRingtone() {
        return isAlarmRingtone;
    }

    public void setAlarmRingtone(boolean alarmRingtone) {
        isAlarmRingtone = alarmRingtone;
    }
}
