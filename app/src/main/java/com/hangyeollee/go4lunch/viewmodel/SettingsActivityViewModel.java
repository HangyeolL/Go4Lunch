package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

public class SettingsActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    public SettingsActivityViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;
    }

    public FirebaseUser getCurrentUser() {
       return mFirebaseRepository.getCurrentUser();
    }

    public void updateUserName(String newName) {
        mFirebaseRepository.updateUserName(newName);
    }

    public void updateUserPhoto(String photoUri) {
        mFirebaseRepository.updateUserPhoto(photoUri);
    }

}
