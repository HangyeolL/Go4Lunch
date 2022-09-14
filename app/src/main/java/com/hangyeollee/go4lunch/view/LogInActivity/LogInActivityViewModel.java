package com.hangyeollee.go4lunch.view.LogInActivity;

import android.location.Location;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

public class LogInActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;
    private LocationRepository mLocationRepository;

    public LogInActivityViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;

    }

    /**
     * Firebase Repository
     */

    public FirebaseAuth getFirebaseInstance() {
        return mFirebaseRepository.getFirebaseInstance();
    }

}

