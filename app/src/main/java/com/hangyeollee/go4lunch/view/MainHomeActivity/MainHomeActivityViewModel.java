package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

import java.util.List;

public class MainHomeActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;
    private LocationRepository mLocationRepository;
    private AutoCompleteDataRepository mAutoCompleteDataRepository;

    private MediatorLiveData<MainHomeActivityViewState> mainHomeActivityViewStateMediatorLiveData = new MediatorLiveData<>();

    private String providerId;

    public MainHomeActivityViewModel(FirebaseRepository firebaseRepository, LocationRepository locationRepository, AutoCompleteDataRepository autoCompleteDataRepository) {
        mFirebaseRepository = firebaseRepository;
        mLocationRepository = locationRepository;
        mAutoCompleteDataRepository = autoCompleteDataRepository;

        LiveData<Location> locationLiveData = mLocationRepository.getLocationLiveData();

        mainHomeActivityViewStateMediatorLiveData.addSource(locationLiveData, location -> {
            combine(location);
        });

    }

    private void combine(Location location) {
        if(location == null) {
            return;
        }

        String providerId = "";
        String userName = "";
        String userEmail = "";
        Uri userPhotoUrl = null;
        boolean isUserLoggedIn = false;

        if (mFirebaseRepository.getCurrentUser() != null) {
            isUserLoggedIn = true;
            userName = mFirebaseRepository.getCurrentUser().getProviderData().get(1).getDisplayName();
            userEmail = mFirebaseRepository.getCurrentUser().getProviderData().get(1).getEmail();
            userPhotoUrl = mFirebaseRepository.getCurrentUser().getProviderData().get(1).getPhotoUrl();
            providerId = mFirebaseRepository.getCurrentUser().getProviderData().get(1).getProviderId();
        } else {
        }

        MainHomeActivityViewState mainHomeActivityViewState = new MainHomeActivityViewState(providerId, userName, userEmail, userPhotoUrl, isUserLoggedIn);

        mainHomeActivityViewStateMediatorLiveData.setValue(mainHomeActivityViewState);
    }

    public LiveData<MainHomeActivityViewState> getMainHomeActivityViewStateLiveData() {
        return mainHomeActivityViewStateMediatorLiveData;
    }

    public void onUserLogInEvent(String providerId) {
        this.providerId = providerId;
        mFirebaseRepository.saveUserInFirestore();
    }

    public String getProviderId() {
        return providerId;
    }

    public void onUserLogOutEvent() {
        mFirebaseRepository.signOutFromFirebaseAuth();
    }

    public void onSearchViewTextChanged(String searchViewText) {
        mAutoCompleteDataRepository.setUserSearchTextQuery(searchViewText);
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