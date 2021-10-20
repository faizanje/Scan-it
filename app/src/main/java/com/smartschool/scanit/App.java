package com.smartschool.scanit;

import android.app.Application;

import com.smartschool.scanit.shared.Config;

import io.paperdb.Paper;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        Config.init();
    }
}
