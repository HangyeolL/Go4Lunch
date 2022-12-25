package com.hangyeollee.go4lunch.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.model.placedetail.MyPlaceDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailDataRepository {

    private final GoogleApi googleApi;

    private static final String FIELDS = "name,photo,vicinity,rating,geometry/location,international_phone_number,opening_hours/open_now,website";

    public PlaceDetailDataRepository(GoogleApi googleApi) {
        this.googleApi = googleApi;
    }

    public LiveData<MyPlaceDetailResponse> getPlaceDetailLiveData(String placeId) {
        MutableLiveData<MyPlaceDetailResponse> placeDetailMutableLiveData = new MutableLiveData<>();

        Call<MyPlaceDetailResponse> call = googleApi.getPlaceDetails(FIELDS, placeId, BuildConfig.PLACES_API_KEY);
        call.enqueue(new Callback<MyPlaceDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyPlaceDetailResponse> call, @NonNull Response<MyPlaceDetailResponse> response) {
                placeDetailMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MyPlaceDetailResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                placeDetailMutableLiveData.setValue(null);
            }
        });

        return placeDetailMutableLiveData;
    }
}
