package com.hangyeollee.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferenceUtil {
    Context mContext;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    public MySharedPreferenceUtil(Context context) {
        mContext = context;
    }

    public SharedPreferences getInstanceOfSharedPref() {
        if (mSharedPreferences == null ) {
            mSharedPreferences = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getInstanceOfEditor() {
        if(mEditor == null) {
            mEditor = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        }
        return mEditor;
    }
}
