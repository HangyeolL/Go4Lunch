package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

public class MainHomeActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;
    private LocationRepository mLocationRepository;

    public MainHomeActivityViewModel(FirebaseRepository firebaseRepository, LocationRepository locationRepository) {
        mFirebaseRepository = firebaseRepository;
        mLocationRepository = locationRepository;
    }

    /**
     * Firebase Repository
     */

    public FirebaseUser getCurrentUser() {
        return mFirebaseRepository.getCurrentUser();
    }

    public void signOutFromFirebaseAuth() {
        mFirebaseRepository.signOutFromFirebaseAuth();
    }

    public void saveUserInFirestore() {
        mFirebaseRepository.saveUserInFirestore();
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

    public LiveData<Location> getLiveLocationLiveData() {
        return mLocationRepository.getLocationLiveData();
    }

}