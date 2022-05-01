package com.hangyeollee.go4lunch.repository;

import android.annotation.SuppressLint;
import android.location.LocationManager;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.NearbySearchApi;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

import retrofit2.Call;

public class NearBySearchRepository {

    private NearbySearchApi mNearbySearchApi;
    private LocationManager mLocationManager;
    private String mLocation;

    @SuppressLint("MissingPermission")
    public NearBySearchRepository(String location, NearbySearchApi nearbySearchApi) {
        mNearbySearchApi = nearbySearchApi;
        mLocation = location;
    }

    public Call<MyNearBySearchData> getNearbySearchData() {
        return MyRetrofitBuilder.getNearBySearchApi().getNearbySearchData(mLocation, 1500, "restaurant", BuildConfig.MAPS_API_KEY);
    }

}
