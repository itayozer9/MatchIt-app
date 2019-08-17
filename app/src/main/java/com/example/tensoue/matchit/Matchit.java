package com.example.tensoue.matchit;

import android.app.Application;
import android.content.Context;

public class Matchit extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Matchit.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Matchit.context;
    }
}