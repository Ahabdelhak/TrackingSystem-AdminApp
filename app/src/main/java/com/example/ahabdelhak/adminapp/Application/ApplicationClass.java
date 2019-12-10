package com.example.ahabdelhak.adminapp.Application;

import android.app.Application;

public class ApplicationClass extends Application {
    private static ApplicationClass singleton;

    public ApplicationClass getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}