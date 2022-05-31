package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

public class LogInAndMainActivitySharedViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    public LogInAndMainActivitySharedViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;
    }

    public FirebaseAuth getFirebaseInstance() {
        return mFirebaseRepository.getFirebaseAuthInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseRepository.getCurrentUser();
    }

    public void updateUserProfile(String newName,String photoUri ) {
        mFirebaseRepository.updateUserProfile(newName, photoUri);
    }

    public void deleteUserFromFirebaseAuth() {
        mFirebaseRepository.deleteUserFromFirebaseAuth();
    }

    public void signOutFromFirebaseAuth() {
        mFirebaseRepository.signOutFromFirebaseAuth();
    }

    /**
     * Firestore
     */

    public Task<DocumentSnapshot> getCurrentUserData(){
        return mFirebaseRepository.getCurrentUserData();
    }

    public void createUser() {
        mFirebaseRepository.createUser();
    }


}

