package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

import java.util.List;

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

    // -----------Firebase method starts----------- //

    public FirebaseUser getCurrentUser() {
        return mFirebaseRepository.getCurrentUser();
    }

    public void setLunchRestaurant(LunchRestaurant lunchRestaurant) {
        mFirebaseRepository.saveLunchRestaurant(lunchRestaurant);
    }

    public void setLikedRestaurant(LikedRestaurant likedRestaurant) {
        mFirebaseRepository.setLikeRestaurant(likedRestaurant);
    }

    public LiveData<List<User>> getUserListWithLunch(String placeId) {
        return mFirebaseRepository.getUserListWithLunch(placeId);
    }

}
