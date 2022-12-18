package com.hangyeollee.go4lunch.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.MyPlaceDetailData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailDataRepository {

    private final GoogleApi googleApi;

    private static final String FIELDS = "name,photo,vicinity,rating,geometry/location,international_phone_number,opening_hours/open_now,website";

    public PlaceDetailDataRepository(GoogleApi googleApi) {
        this.googleApi = googleApi;
    }

    public LiveData<MyPlaceDetailData> getPlaceDetailLiveData(String placeId) {
        MutableLiveData<MyPlaceDetailData> placeDetailMutableLiveData = new MutableLiveData<>();

        Call<MyPlaceDetailData> call = googleApi.getPlaceDetails(FIELDS, placeId, BuildConfig.PLACES_API_KEY);
        call.enqueue(new Callback<MyPlaceDetailData>() {
            @Override
            public void onResponse(@NonNull Call<MyPlaceDetailData> call, @NonNull Response<MyPlaceDetailData> response) {
                placeDetailMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MyPlaceDetailData> call, @NonNull Throwable t) {
                t.printStackTrace();
                placeDetailMutableLiveData.setValue(null);
            }
        });

        return placeDetailMutableLiveData;
    }
}
