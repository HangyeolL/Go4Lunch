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

    private GoogleMapsApi mGoogleMapsApi;

    private MutableLiveData<MyNearBySearchData> mNearBySearchMutableLiveData = new MutableLiveData<>();


    public NearbySearchDataRepository(GoogleMapsApi googleMapsApi) {
        mGoogleMapsApi = googleMapsApi;
    }


    public LiveData<MyNearBySearchData> fetchAndGetMyNearBySearchLiveData (String location) {
        Call<MyNearBySearchData> call = mGoogleMapsApi.getNearbySearchData(location, 1000, "restaurant", BuildConfig.MAPS_API_KEY);
        call.enqueue(new Callback<MyNearBySearchData>() {
            @Override
            public void onResponse(Call<MyNearBySearchData> call, Response<MyNearBySearchData> response) {
                mNearBySearchMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyNearBySearchData> call, Throwable t) {
                mNearBySearchMutableLiveData.postValue(null);
            }
        });

        return mNearBySearchMutableLiveData;
    }
}
