package com.hangyeollee.go4lunch.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.view.activities.MainActivity;

public class MyNotificationUtil {
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    public MyNotificationUtil(Context context) {
        mContext = context;
    }

    public NotificationManager getInstance() {
        if(mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public void buildNotification1() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel 1", "Today's lunch", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Here is your today's lunch");
            getInstance().createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(mContext, 1, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        mNotificationBuilder = new NotificationCompat.Builder(mContext, "channel 1")
                .setSmallIcon(R.drawable.ic_baseline_local_dining_24)
                .setContentTitle("Today's lunch")
                .setContentText("you will eat at")
                .setContentIntent(pendingNotificationIntent);
    }

    public void notifyNotification1() {
        getInstance().notify("Go4Lunch", 1, mNotificationBuilder.build());
    }


}
