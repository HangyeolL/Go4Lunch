package com.hangyeollee.go4lunch.view.LogInActivity;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

public class LogInActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    public LogInActivityViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;

    }

    public FirebaseAuth getFirebaseInstance() {
        return mFirebaseRepository.getFirebaseInstance();
    }

}

