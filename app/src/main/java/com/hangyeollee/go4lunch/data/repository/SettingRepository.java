package com.hangyeollee.go4lunch.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.hangyeollee.go4lunch.utils.BooleanSharedPreferencesLiveData;

public class SettingRepository {

    private static final String KEY_SHARED_PREFS_SETTINGS = "SETTINGS";
    private static final String KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED = "KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED";

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public SettingRepository(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE);
    }

    // GET
    public LiveData<Boolean> getIsNotificationEnabledLiveData() {
        return new BooleanSharedPreferencesLiveData(context, KEY_SHARED_PREFS_SETTINGS, KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED);
    }

    // INSERT
    public void setNotificationEnable(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED, enabled).apply();
    }
}
