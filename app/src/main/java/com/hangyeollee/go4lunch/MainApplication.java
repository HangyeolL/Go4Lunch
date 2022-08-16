package com.hangyeollee.go4lunch;

import android.app.Application;

public class MainApplication extends Application {
    private static MainApplication sInstance;

    public static Application getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }
}
