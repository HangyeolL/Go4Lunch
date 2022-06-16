package com.hangyeollee.go4lunch.viewmodel;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

public class MainActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;
    private AutoCompleteDataRepository mAutoCompleteDataRepository;
    private LocationRepository mLocationRepository;

    public MainActivityViewModel(FirebaseRepository firebaseRepository, AutoCompleteDataRepository autoCompleteDataRepository, LocationRepository locationRepository) {
        mFirebaseRepository = firebaseRepository;
        mAutoCompleteDataRepository = autoCompleteDataRepository;
        mLocationRepository = locationRepository;
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseRepository.getCurrentUser();
    }

    public void signOutFromFirebaseAuth() {
        mFirebaseRepository.signOutFromFirebaseAuth();
    }

    public void saveUserInFirestore() {
        mFirebaseRepository.saveUserInFirestore();
    }

    public LiveData<MyAutoCompleteData> getAutoCompleteLiveData() {
        return mAutoCompleteDataRepository.getAutoCompleteLiveData();
    }

    public void fetchAutoCompleteData(String input, String location) {
        mAutoCompleteDataRepository.fetchData(input, location);
    }

    public LiveData<Location> getLocationLiveData() {
        return mLocationRepository.getLocationLiveData();
    }

    public void startLocationRequest() {
        mLocationRepository.stopLocationRequest();
    }

    public void stopLocationRequest() {
        mLocationRepository.stopLocationRequest();
    }

}