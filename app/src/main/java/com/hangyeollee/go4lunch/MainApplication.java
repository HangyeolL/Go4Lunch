package com.hangyeollee.go4lunch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MainApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_ID_1";

    private static MainApplication sInstance;

    public static Application getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel
                            (CHANNEL_ID, sInstance.getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription(sInstance.getString(R.string.channel_description));

            // Register the channel1 with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }

}
