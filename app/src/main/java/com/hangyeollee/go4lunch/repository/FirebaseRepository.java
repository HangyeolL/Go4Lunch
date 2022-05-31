package com.hangyeollee.go4lunch.repository;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hangyeollee.go4lunch.utility.MyFirestoreUtil;

public class FirebaseRepository {

    private static FirebaseAuth FIREBASEAUTH;
    private FirebaseFirestore mFirestore;

    // Dependency Injection for unit test purpose
    public FirebaseRepository(FirebaseFirestore firestore) {
        mFirestore = firestore;
    }

    /**
     * -----------FirebaseAuth-----------
     */

    public FirebaseAuth getFirebaseAuthInstance() {
        if (FIREBASEAUTH != null) {
            return FIREBASEAUTH;
        }
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }

    public void updateUserProfile(String newName, String photoUri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newName).setPhotoUri(Uri.parse(photoUri)).build();

        getCurrentUser().updateProfile(profileUpdates);
    }

    public void deleteUserFromFirebaseAuth() {
        getCurrentUser().delete();
    }

    public void signOutFromFirebaseAuth() {
        getFirebaseAuthInstance().signOut();
    }

    /**
     * -----------Firestore-----------
     */

    public FirebaseFirestore getFirestoreInstance() {
        return MyFirestoreUtil.getFirestoreInstance();
    }

    public CollectionReference getUsersCollection() {
        return getFirestoreInstance().collection("users");
    }

    // Get CurrentUser Data from Firestore
    public Task<DocumentSnapshot> getCurrentUserData(){
        String uid = getCurrentUser().getUid();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }

    public void createUser() {
        FirebaseUser userToCreate = getCurrentUser();
        if (userToCreate != null) {
            String urlPicture = (userToCreate.getPhotoUrl() != null) ? userToCreate.getPhotoUrl().toString() : null;
            String username = userToCreate.getDisplayName();
            String uid = userToCreate.getUid();

            Task<DocumentSnapshot> userData = getCurrentUserData();
            userData.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    getUsersCollection().document(uid).set(userToCreate);
                }
            });
        }
    }

    // Update User Username
    public Task<Void> updateCurrentUsername(String username) {
        String uid = getCurrentUser().getUid();
        if(uid != null){
            return this.getUsersCollection().document(uid).update("name", username);
        }else{
            return null;
        }
    }

    public void setLikeRestaurant() {

    }

    //    public void addFirebaseAuthUserInFirestore(FirebaseUser firebaseUser) {
    //        userData.put(FIELD_NAME, firebaseUser.getDisplayName());
    //        userData.put(FIELD_PHOTO_URL, firebaseUser.getPhotoUrl());
    //        getFirestoreInstance().collection(COLLECTION_USERS).document(DOCUMENT_INFO).set(userData);
    //    }
    //
    //    public void setChosenRestaurantOfUserInFirestore(User user) {
    //        userData.put(FIELD_NAME, user.getChosenRestaurantName());
    //        userData.put(FIELD_ID, user.getRestaurantId());
    //        userData.put(FIELD_DATE, user.getDate());
    //        getFirestoreInstance().collection(COLLECTION_USERS).document(DOCUMENT_CHOSEN_RESTAURANT).set(userData);
    //    }


}
