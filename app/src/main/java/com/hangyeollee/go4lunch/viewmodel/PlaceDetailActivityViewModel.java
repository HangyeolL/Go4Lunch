package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

public class PlaceDetailActivityViewModel extends ViewModel {

    private PlaceDetailDataRepository mPlaceDetailDataRepository;

    public PlaceDetailActivityViewModel(PlaceDetailDataRepository placeDetailDataRepository) {
        mPlaceDetailDataRepository = placeDetailDataRepository;
    }

    public void fetchPlaceDetailData(String placeId) {
        mPlaceDetailDataRepository.fetchData(placeId);
    }

    public LiveData<MyPlaceDetailData> getPlaceDetailLiveData() {
        return mPlaceDetailDataRepository.getPlaceDetailLiveData();
    }
}
