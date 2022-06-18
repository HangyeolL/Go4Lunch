package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivityViewModel extends ViewModel {

    private PlaceDetailDataRepository mPlaceDetailDataRepository;
    private FirebaseRepository mFirebaseRepository;

    private MutableLiveData<List<User>> mSortedUserListLiveData = new MutableLiveData<>();

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

    public LiveData<List<User>> getUsersList() {
        return mFirebaseRepository.getUsersList();
    }

    public LiveData<List<LunchRestaurant>> getLunchRestaurantListOfAllUsers() {
        return mFirebaseRepository.getLunchRestaurantListOfAllUsers();
    }

    public LiveData<List<User>> getSortedUserList(List<User> userList, List<LunchRestaurant> lunchRestaurantList) {
        List<User> sortedUserList = new ArrayList<>();
        for (User user : userList) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (user.getId().equals(lunchRestaurant.getUserId())) {
                    sortedUserList.add(user);
                    mSortedUserListLiveData.setValue(sortedUserList);
                    break;
                }
            }
        }
        return mSortedUserListLiveData;
    }

    public LiveData<List<User>> getSortedUserList2() {
        List<User> sortedUserList = new ArrayList<>();
        List<User> userList = mFirebaseRepository.getUsersList2();
        List<LunchRestaurant> lunchRestaurantList = mFirebaseRepository.getLunchRestaurantListOfAllUsers2();

        for (User user : userList) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (user.getId().equals(lunchRestaurant.getUserId())) {
                    sortedUserList.add(user);
                    mSortedUserListLiveData.setValue(sortedUserList);
                    break;
                }
            }
        }
        return mSortedUserListLiveData;
    }
}