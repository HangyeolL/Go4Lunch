package com.hangyeollee.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

import java.util.List;

public class FirebaseViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    public FirebaseViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;
    }

    public FirebaseAuth getFirebaseInstance() {
        return mFirebaseRepository.getFirebaseAuthInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseRepository.getCurrentUser();
    }

    public void updateUserProfile(String newName, String photoUri) {
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

    public void saveUserInFirestore() {
        mFirebaseRepository.saveUserInFirestore();
    }

    public LiveData<List<User>> getUsersList() {
        return mFirebaseRepository.getUsersList();
    }

    public LiveData<List<LunchRestaurant>> getLunchRestaurantListOfAllUsers() {
        return mFirebaseRepository.getLunchRestaurantListOfAllUsers();
    }
}

