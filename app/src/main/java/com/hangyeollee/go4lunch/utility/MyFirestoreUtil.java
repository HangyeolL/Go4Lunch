package com.hangyeollee.go4lunch.utility;

import com.google.firebase.firestore.FirebaseFirestore;

public class MyFirestoreUtil {
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }
}
