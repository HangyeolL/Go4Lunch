package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

public class MainHomeActivityViewModel extends ViewModel {

    private final FirebaseRepository firebaseRepository;
    private final LocationRepository locationRepository;
    private final AutoCompleteDataRepository autoCompleteDataRepository;

    private final MediatorLiveData<MainHomeActivityViewState> mainHomeActivityViewStateMediatorLiveData = new MediatorLiveData<>();

    private String providerId;

    public MainHomeActivityViewModel(FirebaseRepository firebaseRepository, LocationRepository locationRepository, AutoCompleteDataRepository autoCompleteDataRepository) {
        this.firebaseRepository = firebaseRepository;
        this.locationRepository = locationRepository;
        this.autoCompleteDataRepository = autoCompleteDataRepository;

        LiveData<Location> locationLiveData = this.locationRepository.getLocationLiveData();

        mainHomeActivityViewStateMediatorLiveData.addSource(locationLiveData, this::combine);

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

        if (firebaseRepository.getCurrentUser() != null) {
            isUserLoggedIn = true;
            userName = firebaseRepository.getCurrentUser().getProviderData().get(1).getDisplayName();
            userEmail = firebaseRepository.getCurrentUser().getProviderData().get(1).getEmail();
            userPhotoUrl = firebaseRepository.getCurrentUser().getProviderData().get(1).getPhotoUrl();
            providerId = firebaseRepository.getCurrentUser().getProviderData().get(1).getProviderId();
        }

        MainHomeActivityViewState mainHomeActivityViewState = new MainHomeActivityViewState(providerId, userName, userEmail, userPhotoUrl, isUserLoggedIn);

        mainHomeActivityViewStateMediatorLiveData.setValue(mainHomeActivityViewState);
    }

    public LiveData<MainHomeActivityViewState> getMainHomeActivityViewStateLiveData() {
        return mainHomeActivityViewStateMediatorLiveData;
    }

    public void onUserLogInEvent(String providerId) {
        this.providerId = providerId;
        firebaseRepository.saveUserInFirestore();
    }

    public String getProviderId() {
        return providerId;
    }

    public void onUserLogOutEvent() {
        firebaseRepository.signOutFromFirebaseAuth();
    }

    public void onSearchViewTextChanged(String searchViewText) {
        autoCompleteDataRepository.setUserSearchTextQuery(searchViewText);
    }

    /**
     * Location Repository
     */

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
    }


}