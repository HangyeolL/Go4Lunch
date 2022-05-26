package com.hangyeollee.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailDataRepository {

    private GoogleMapsApi mGoogleMapsApi;
    private MutableLiveData<MyPlaceDetailData> mPlaceDetailMutableLiveData = new MutableLiveData<>();

    private Result mResult;

    private static final String FIELDS = "name,photo,vicinity,rating,geometry/location,international_phone_number,opening_hours/open_now,website";

    public PlaceDetailDataRepository(GoogleMapsApi googleMapsApi) {
        mGoogleMapsApi = googleMapsApi;
    }

    public LiveData<MyPlaceDetailData> getPlaceDetailLiveData() {
        return mPlaceDetailMutableLiveData;
    }

    public Result getPlaceDetailResult() {
        return mResult;
    }

    public void fetchData(String placeId) {
        Call<MyPlaceDetailData> call = mGoogleMapsApi.getPlaceDetails(FIELDS, placeId, BuildConfig.MAPS_API_KEY);
        call.enqueue(new Callback<MyPlaceDetailData>() {
            @Override
            public void onResponse(Call<MyPlaceDetailData> call, Response<MyPlaceDetailData> response) {
                mPlaceDetailMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyPlaceDetailData> call, Throwable t) {
                mPlaceDetailMutableLiveData.postValue(null);
            }
        });
    }
}
