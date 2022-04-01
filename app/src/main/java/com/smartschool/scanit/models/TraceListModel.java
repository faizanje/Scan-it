package com.smartschool.scanit.models;

public class TraceListModel {
    String title,index,time,status;

    public TraceListModel(String title, String index, String time, String status) {
        this.title = title;
        this.index = index;
        this.time = time;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getIndex() {
        return index;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
