package com.hangyeollee.go4lunch.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SettingRepository {

    private static final String KEY_SHARED_PREFS_SETTINGS = "settings";
    private static final String KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED = "KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED";

    private final SharedPreferences sharedPreferences;

    public SettingRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE);
    }

    // GET
    public LiveData<Boolean> areNotificationsEnabledLiveData() {
        MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>(
            sharedPreferences.getBoolean(KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED, false)
        );

        // TODO Hangyeol check the "Java strong/weak reference" stuff online !
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED.equalsIgnoreCase(key)) {
                    mutableLiveData.setValue(sharedPreferences.getBoolean(key, false));
                }
            }
        });

        return mutableLiveData;
    }

    // INSERT
    public void areNotificationEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED, enabled).apply();
    }
}
