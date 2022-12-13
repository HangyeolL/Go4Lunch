package com.hangyeollee.go4lunch.utils;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.Locale;

public class DistanceCalculator {

    @NonNull
    public String distanceBetween(double lat1, double long1, double lat2, double long2) {
        Location location1 = new Location("");
        location1.setLatitude(lat1);
        location1.setLongitude(long1);

        Location location2 = new Location("");
        location2.setLatitude(lat2);
        location2.setLongitude(long2);

        return String.format(Locale.getDefault(), "%.0f", location1.distanceTo(location2)) + "m";
    }
}
