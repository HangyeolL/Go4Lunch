package com.hangyeollee.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;

public class PlaceDetailDataRepository {

    private GoogleMapsApi mGoogleMapsApi;
    private MutableLiveData<MyPlaceDetailData> mPlaceDetailMutableLiveData = new MutableLiveData<>();

    public PlaceDetailDataRepository(GoogleMapsApi googleMapsApi) {
        mGoogleMapsApi = googleMapsApi;
    }

    public LiveData<MyPlaceDetailData> getPlaceDetailLiveData() {
        return mPlaceDetailMutableLiveData;
    }

//    public void fetchData() {
//        Call<MyPlaceDetailData> call = mGoogleMapsApi.getPlaceDetails(location, 1500, "restaurant", BuildConfig.MAPS_API_KEY);
//        call.enqueue(new Callback<MyNearBySearchData>() {
//            @Override
//            public void onResponse(Call<MyNearBySearchData> call, Response<MyNearBySearchData> response) {
//                mNearBySearchMutableLiveData.setValue(response.body());
//                Log.d("Retrofit", response.raw().request().url().toString());
//
//                mResultList = response.body().getResults();
//            }
//            @Override
//            public void onFailure(Call<MyNearBySearchData> call, Throwable t) {
//                mNearBySearchMutableLiveData.postValue(null);
//            }
//        });
//    }
}
