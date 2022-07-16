package com.hangyeollee.go4lunch.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferenceUtil {
    Context mContext;
    SharedPreferences.Editor mEditor;

    public MySharedPreferenceUtil(Context context) {
        mContext = context;
    }

    public SharedPreferences.Editor getInstance() {
        if(mEditor == null) {
            mEditor = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        }
        return mEditor;
    }
}
