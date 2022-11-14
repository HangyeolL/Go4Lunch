package com.hangyeollee.go4lunch.ui.logIn_activity;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;

public class LogInViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    public LogInViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;

    }

    public FirebaseAuth getFirebaseInstance() {
        return mFirebaseRepository.getFirebaseInstance();
    }

}

