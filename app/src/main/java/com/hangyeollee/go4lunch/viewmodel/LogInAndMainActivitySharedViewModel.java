package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.repository.FirebaseAuthRepository;

public class LogInAndMainActivitySharedViewModel extends ViewModel {

    private FirebaseAuthRepository mFirebaseAuthRepository;

    public LogInAndMainActivitySharedViewModel(FirebaseAuthRepository firebaseAuthRepository) {
        mFirebaseAuthRepository = firebaseAuthRepository;
    }

    public FirebaseAuth getFirebaseInstance() {
        return mFirebaseAuthRepository.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseAuthRepository.getCurrentUser();
    }

    public void updateUserProfile(String newName,String photoUri ) {
        mFirebaseAuthRepository.updateUserProfile(newName, photoUri);
    }

    public void deleteUser() {
        mFirebaseAuthRepository.deleteUser();
    }

    public void signOut() {
        mFirebaseAuthRepository.signOut();
    }
}

