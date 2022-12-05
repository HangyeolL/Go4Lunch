package com.hangyeollee.go4lunch.utils;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hangyeollee.go4lunch.MainApplication;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.ui.dispatcher.DispatcherActivity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    private final Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);

        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        List<LunchRestaurant> lunchRestaurantList = getLunchRestaurantList();

        if (didUserChooseLunchRestaurant(lunchRestaurantList)) {
            createNotification(lunchRestaurantList);
        }

        return Result.success();
        // (Returning RETRY tells WorkManager to try this task again
        // later; FAILURE says not to try again.)
    }

    private void createNotification(List<LunchRestaurant> lunchRestaurantList) {
        Bitmap bitmap = null;
        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            try {
                bitmap = MediaStore.Images.Media
                    .getBitmap(context.getContentResolver(), FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
            } catch (IOException e) {
                Log.d("Hangyeol", "photoUrl to bitmap converting failed");
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_baseline_person_outline_24);
        }

        Intent dispatcherIntent = DispatcherActivity.navigate(context);
        dispatcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, dispatcherIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_local_dining_24)
            .setShowWhen(true)
            .setLargeIcon(bitmap)
            .setContentTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
            .setContentText(context.getString(R.string.you_will_go_to) + getUsersLunchRestaurantName(lunchRestaurantList))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    private List<LunchRestaurant> getLunchRestaurantList() {
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        FirebaseFirestore.getInstance().collection(LocalDate.now().toString()).addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                return;
            }
            if (querySnapshot != null) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    LunchRestaurant lunchRestaurant = document.toObject(LunchRestaurant.class);
                    lunchRestaurantList.add(lunchRestaurant);
                }
                latch.countDown();
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lunchRestaurantList;
    }

    private boolean didUserChooseLunchRestaurant(List<LunchRestaurant> lunchRestaurantList) {
        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(lunchRestaurant.getUserId())) {
                return true;
            }
        }
        return false;
    }

    private String getUsersLunchRestaurantName(List<LunchRestaurant> lunchRestaurantList) {
        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(lunchRestaurant.getUserId())) {
                return lunchRestaurant.getRestaurantName();
            }
        }
        return null;
    }
}
