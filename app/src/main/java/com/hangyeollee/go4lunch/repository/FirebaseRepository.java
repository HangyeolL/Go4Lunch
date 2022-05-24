package com.hangyeollee.go4lunch.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseRepository {

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void deleteUser() {

    }
}
