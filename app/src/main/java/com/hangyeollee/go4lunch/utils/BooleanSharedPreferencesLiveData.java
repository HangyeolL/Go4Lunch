package com.hangyeollee.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class BooleanSharedPreferencesLiveData extends MutableLiveData<Boolean> {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.OnSharedPreferenceChangeListener listener;

    public BooleanSharedPreferencesLiveData(@NonNull Context context, @NonNull String fileName, @NonNull String key) {
        super();

        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        setValue(sharedPreferences.getBoolean(key, false));
        listener = (sharedPreferences, changedKey) -> {
            if (key.equalsIgnoreCase(changedKey)) {
                setValue(sharedPreferences.getBoolean(key, false));
            }
        };
    }

    @Override
    protected void onActive() {
        super.onActive();

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
