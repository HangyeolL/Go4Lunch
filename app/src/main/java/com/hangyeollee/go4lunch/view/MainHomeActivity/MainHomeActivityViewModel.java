package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.annotation.SuppressLint;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

import java.util.List;

public class MainHomeActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;
    private LocationRepository mLocationRepository;

    private MutableLiveData<MainHomeActivityViewState> mainHomeActivityViewStateMutableLiveData = new MutableLiveData<>();

    public MainHomeActivityViewModel(FirebaseRepository firebaseRepository, LocationRepository locationRepository) {
        mFirebaseRepository = firebaseRepository;
        mLocationRepository = locationRepository;

        transformation();
    }

    private void transformation() {
        String providerId = "";
        String userName = "";
        String userEmail = "";
        Uri userPhotoUrl = null;
        boolean isUserLoggedIn = false;
        List<? extends UserInfo> userInfoList = null;

        if (mFirebaseRepository.getCurrentUser() != null) {
            isUserLoggedIn = true;
            userName = mFirebaseRepository.getCurrentUser().getDisplayName();
            userEmail = mFirebaseRepository.getCurrentUser().getEmail();

            if(mFirebaseRepository.getCurrentUser().getPhotoUrl() != null) {
                userPhotoUrl = mFirebaseRepository.getCurrentUser().getPhotoUrl();
            }

            userInfoList = mFirebaseRepository.getCurrentUser().getProviderData();
            providerId = mFirebaseRepository.getCurrentUser().getProviderId();

        } else {
            mainHomeActivityViewStateMutableLiveData.setValue(null);
        }

        MainHomeActivityViewState mainHomeActivityViewState = new MainHomeActivityViewState(providerId, userName, userEmail, userPhotoUrl, isUserLoggedIn, userInfoList);

        mainHomeActivityViewStateMutableLiveData.setValue(mainHomeActivityViewState);
    }

    public LiveData<MainHomeActivityViewState> getMainHomeActivityViewStateLiveData() {
        return mainHomeActivityViewStateMutableLiveData;
    }

    public void onUserLogInEvent() {
        mFirebaseRepository.saveUserInFirestore();
    }

    public void signOutFromFirebaseAuth() {
        mFirebaseRepository.signOutFromFirebaseAuth();
    }



    /**
     * Location Repository
     */

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        mLocationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        mLocationRepository.stopLocationRequest();
    }

}