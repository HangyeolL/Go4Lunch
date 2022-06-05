package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

public class PlaceDetailActivityViewModel extends ViewModel {

    private PlaceDetailDataRepository mPlaceDetailDataRepository;
    private FirebaseRepository mFirebaseRepository;

    public PlaceDetailActivityViewModel(PlaceDetailDataRepository placeDetailDataRepository, FirebaseRepository firebaseRepository) {
        mPlaceDetailDataRepository = placeDetailDataRepository;
        mFirebaseRepository = firebaseRepository;
    }

    public void fetchPlaceDetailData(String placeId) {
        mPlaceDetailDataRepository.fetchData(placeId);
    }

    public LiveData<MyPlaceDetailData> getPlaceDetailLiveData() {
        return mPlaceDetailDataRepository.getPlaceDetailLiveData();
    }

    public void setLunchRestaurant(LunchRestaurant lunchRestaurant) {
        mFirebaseRepository.saveLunchRestaurant(lunchRestaurant);
    }

    public void getLunchRestaurant() {
        mFirebaseRepository.getLunchRestaurant();
    }

//    public void deleteLunchRestaurant(LunchRestaurant lunchRestaurant) {
//        mFirebaseRepository.deleteLunchRestaurant(lunchRestaurant);
//    }
//
//    public void setLikedRestaurant(LikedRestaurant likedRestaurant) {
//        mFirebaseRepository.setLikedRestaurant(likedRestaurant);
//    }

}
