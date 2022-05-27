package com.hangyeollee.go4lunch.repository;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseAuthRepository {

    private static FirebaseAuth INSTANCE;

    public FirebaseAuth getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return getInstance().getCurrentUser();
    }

    public void updateUserProfile(String newName,String photoUri ) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        getCurrentUser().updateProfile(profileUpdates);
    }

    public void deleteUser() {
        getCurrentUser().delete();
    }

    public void signOut() {
        getInstance().signOut();
    }


}
