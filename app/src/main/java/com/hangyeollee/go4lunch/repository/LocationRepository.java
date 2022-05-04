package com.hangyeollee.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationRepository {
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    private LocationCallback callback;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);

    public LocationRepository(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    @SuppressLint("MissingPermission")
    public LiveData<Location> getLocationLiveData() {
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                locationMutableLiveData.setValue(locationResult.getLastLocation());
            }
        };
        fusedLocationProviderClient.removeLocationUpdates(callback);
        fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER).setInterval(LOCATION_REQUEST_INTERVAL_MS),callback
            , Looper.getMainLooper());
        return locationMutableLiveData;
    }

}
