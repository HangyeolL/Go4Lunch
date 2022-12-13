package com.hangyeollee.go4lunch.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SettingRepositoryTest {

    private Context context;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        context = Mockito.mock(Context.class);
        sharedPreferences = Mockito.mock(SharedPreferences.class);

        Mockito.doReturn(sharedPreferences).when(context).getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
    }

    @Test
    public void setNotificationEnabled_true() {
        // Given
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.doReturn(editor).when(sharedPreferences).edit();
        Mockito.doReturn(editor).when(editor).putBoolean("KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED", true);

        // When
        new SettingRepository(context).setNotificationEnabled(true);

        // Then
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean("KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED", true);
        Mockito.verify(editor).apply();
    }

    @Test
    public void setNotificationEnabled_false() {
        // Given
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.doReturn(editor).when(sharedPreferences).edit();
        Mockito.doReturn(editor).when(editor).putBoolean("KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED", false);

        // When
        new SettingRepository(context).setNotificationEnabled(false);

        // Then
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean("KEY_SHARED_PREFS_SETTINGS_NOTIFICATION_ENABLED", false);
        Mockito.verify(editor).apply();
    }

}