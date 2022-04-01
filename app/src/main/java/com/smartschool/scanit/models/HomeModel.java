package com.smartschool.scanit.models;

public class HomeModel {
    String title;
    int img;
    int color;

    public HomeModel(String title, int img, int color) {
        this.title = title;
        this.img = img;
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getImg() {
        return img;
    }

    public int getColor() {
        return color;
    }
}
