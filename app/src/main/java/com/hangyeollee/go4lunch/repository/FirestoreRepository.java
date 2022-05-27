package com.hangyeollee.go4lunch.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hangyeollee.go4lunch.model.User;

public class FirestoreRepository {

    private static FirebaseFirestore INSTANCE;

    private final CollectionReference mCollectionReference = INSTANCE.collection("users");

    public static FirebaseFirestore getInstance() {
        if(INSTANCE != null) {
            return INSTANCE;

        }
        return FirebaseFirestore.getInstance();
    }


    public void createUser(User user) {
        mCollectionReference.add(user);
    }


}
