package com.hangyeollee.go4lunch.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

public class resourceToUri {
    public static String resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        context.getResources().getResourcePackageName(resID) + '/' +
                        context.getResources().getResourceTypeName(resID) + '/' +
                        context.getResources().getResourceEntryName(resID))
                .toString();
    }
}
