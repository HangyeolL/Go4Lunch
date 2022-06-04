package com.hangyeollee.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.utility.MyFirestoreUtil;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private static FirebaseAuth FIREBASEAUTH;
    private FirebaseFirestore mFirestore;

    private MutableLiveData<List<User>> mUserListMutableLiveData = new MutableLiveData<>();
    private List<User> mUserList;

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

    public CollectionReference getLunchRestaurantCollection() {
        return getUsersCollection().document(getCurrentUser().getUid()).collection("lunchRestaurant");
    }

    public CollectionReference getLikedRestaurantCollection() {
        return getUsersCollection().document(getCurrentUser().getUid()).collection("likedRestaurant");
    }

    public void saveUserInFirestore() {
        String photoUrl = getCurrentUser().getPhotoUrl().toString();
        String username = getCurrentUser().getDisplayName();

        User userToCreate = new User(username, photoUrl);

        getUsersCollection().document(getCurrentUser().getUid()).set(userToCreate, SetOptions.merge()).addOnSuccessListener(v -> {
            Log.e("Firestore", "user successfully stored !");
        }).addOnFailureListener(v -> {
            Log.e("Firestore", "user saving failed");
        });
    }

    public LiveData<List<User>> getAllUsers() {
        getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mUserList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        mUserList.add(user);
                    }
                    mUserListMutableLiveData.setValue(mUserList);
                    Log.e("Firestore", "Success getting all of user documents");
                } else {
                    Log.d("Firestore", "Error getting all of users documents: ", task.getException());
                    mUserListMutableLiveData.postValue(null);
                }
            }
        });
        return mUserListMutableLiveData;
    }

    public void setLunchRestaurant(LunchRestaurant lunchRestaurant) {
        getLunchRestaurantCollection().document(lunchRestaurant.getId()).set(lunchRestaurant, SetOptions.merge()).addOnSuccessListener(success -> {
            Log.e("LunchRestau", "Successfully stored");
        }).addOnFailureListener(failure -> {
            Log.e("LunchRestau", "Failed storing");
        });
    }

    public void setLikedRestaurant(LikedRestaurant likedRestaurant) {

    }

}
