package com.hangyeollee.go4lunch.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyNotificationUtil myNotificationUtil = new MyNotificationUtil(context);

        myNotificationUtil.buildNotification1();
        myNotificationUtil.notifyNotification1();
    }

}
