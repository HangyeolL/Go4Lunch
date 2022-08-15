package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.PlaceDetailActivityViewState;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivityViewModel extends ViewModel {

    private PlaceDetailDataRepository mPlaceDetailDataRepository;
    private FirebaseRepository mFirebaseRepository;

    private MediatorLiveData<PlaceDetailActivityViewState> mMediatorLiveData = new MediatorLiveData<>();

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

    public LiveData<List<LikedRestaurant>> getLikedRestaurantList() {
        return mFirebaseRepository.getLikedRestaurantList();
    }

    public void setLikedRestaurant(LikedRestaurant likedRestaurant) {
        mFirebaseRepository.setLikeRestaurant(likedRestaurant);
    }

    public LiveData<PlaceDetailActivityViewState> getMediatorLiveData() {
        return mMediatorLiveData;
    }

    public void fetchMediatorLivedata() {
        mMediatorLiveData.addSource(mPlaceDetailDataRepository.getPlaceDetailLiveData(), placeDetailData -> {
            PlaceDetailActivityViewModel.this.combineDataToMediatorLivedata(
                    placeDetailData,
                    mFirebaseRepository.getUsersList().getValue(),
                    mFirebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                    mFirebaseRepository.getLikedRestaurantList().getValue());
        });

        mMediatorLiveData.addSource(mFirebaseRepository.getUsersList(), userList -> {
            PlaceDetailActivityViewModel.this.combineDataToMediatorLivedata(
                    mPlaceDetailDataRepository.getPlaceDetailLiveData().getValue(),
                    userList,
                    mFirebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                    mFirebaseRepository.getLikedRestaurantList().getValue());
        });

        mMediatorLiveData.addSource(mFirebaseRepository.getLunchRestaurantListOfAllUsers(), lunchRestaurantList -> {
            PlaceDetailActivityViewModel.this.combineDataToMediatorLivedata(
                    mPlaceDetailDataRepository.getPlaceDetailLiveData().getValue(),
                    mFirebaseRepository.getUsersList().getValue(),
                    lunchRestaurantList,
                    mFirebaseRepository.getLikedRestaurantList().getValue());
        });

        mMediatorLiveData.addSource(mFirebaseRepository.getLikedRestaurantList(), likedRestaurantList -> {
            PlaceDetailActivityViewModel.this.combineDataToMediatorLivedata(
                    mPlaceDetailDataRepository.getPlaceDetailLiveData().getValue(),
                    mFirebaseRepository.getUsersList().getValue(),
                    mFirebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                    likedRestaurantList
            );
        });


    }

    private void combineDataToMediatorLivedata(MyPlaceDetailData myPlaceDetailData, List<User> userList, List<LunchRestaurant> lunchRestaurantList, List<LikedRestaurant> likedRestaurantList) {
        if (myPlaceDetailData == null || userList == null || lunchRestaurantList == null || likedRestaurantList == null) {
            return;
        }

        List<User> sortedUserList = new ArrayList<>();

        for (User user : userList) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (user.getId().equals(lunchRestaurant.getUserId()) &&
                        myPlaceDetailData.getResult().getName().equals(lunchRestaurant.getName())) {
                    sortedUserList.add(user);
                }
            }
        }

        Boolean isSelectedAsLikedRestaurant = false;

        for (LikedRestaurant likedRestaurant : likedRestaurantList) {
            if(myPlaceDetailData.getResult().getName().equals(likedRestaurant.getName())) {
                isSelectedAsLikedRestaurant = true;
                break;
            }
        }

        Boolean isSelectedAsLunchRestaurant = false;

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            if(myPlaceDetailData.getResult().getName().equals(lunchRestaurant.getName())) {
                isSelectedAsLunchRestaurant = true;
                break;
            }
        }


        PlaceDetailActivityViewState placeDetailActivityViewState =
                new PlaceDetailActivityViewState(sortedUserList, isSelectedAsLikedRestaurant,isSelectedAsLunchRestaurant, myPlaceDetailData.getResult());

        mMediatorLiveData.setValue(placeDetailActivityViewState);
    }

}