package com.hangyeollee.go4lunch.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbySearchDataRepository {

    private final GoogleApi googleApi;

    public NearbySearchDataRepository(GoogleApi googleApi) {
        this.googleApi = googleApi;
    }

    public LiveData<MyNearBySearchData> fetchAndGetMyNearBySearchLiveData (String location) {
        MutableLiveData<MyNearBySearchData> nearBySearchMutableLiveData = new MutableLiveData<>();

        Call<MyNearBySearchData> call = googleApi.getNearbySearchData(location, 3000, "restaurant", BuildConfig.PLACES_API_KEY);
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
