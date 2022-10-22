package com.hangyeollee.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbySearchDataRepository {

    private final GoogleMapsApi googleMapsApi;

    public NearbySearchDataRepository(GoogleMapsApi googleMapsApi) {
        this.googleMapsApi = googleMapsApi;
    }


    public LiveData<MyNearBySearchData> fetchAndGetMyNearBySearchLiveData (String location) {
        MutableLiveData<MyNearBySearchData> nearBySearchMutableLiveData = new MutableLiveData<>();

        Call<MyNearBySearchData> call = googleMapsApi.getNearbySearchData(location, 1000, "restaurant", BuildConfig.PLACES_API_KEY);
        call.enqueue(new Callback<MyNearBySearchData>() {
            @Override
            public void onResponse(Call<MyNearBySearchData> call, Response<MyNearBySearchData> response) {
                nearBySearchMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyNearBySearchData> call, Throwable t) {
                nearBySearchMutableLiveData.postValue(null);
            }
        });

        return nearBySearchMutableLiveData;
    }
}
