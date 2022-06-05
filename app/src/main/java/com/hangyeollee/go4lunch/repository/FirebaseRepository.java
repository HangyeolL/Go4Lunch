package com.hangyeollee.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.utility.MyFirestoreUtil;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private static FirebaseAuth FIREBASEAUTH;
    private FirebaseFirestore mFirestore;

    private MutableLiveData<List<User>> mUserListMutableLiveData = new MutableLiveData<>();

    private LunchRestaurant mLunchRestaurant;

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
        return getFirestoreInstance().collection("lunchRestaurant");
    }

    public CollectionReference getLikedRestaurantCollection() {
        return getFirestoreInstance().collection("likedRestaurant");
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

    public LiveData<List<User>> subscribeToUsersCollectionSnapshotListener() {
        getUsersCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("userCollection", "Listen failed.", error);
                    mUserListMutableLiveData.postValue(null);
                    return;
                }
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                mUserListMutableLiveData.setValue(userList);
            }
        });
        return mUserListMutableLiveData;
    }

    public void saveLunchRestaurant(LunchRestaurant lunchRestaurant) {
        getLunchRestaurantCollection().document(getCurrentUser().getUid()).set(lunchRestaurant, SetOptions.merge());
    }

//    public LiveData<> subscribeToLunchRestaurantDocumentSnapshotListener() {
//        getLunchRestaurantCollection().document(getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                if(error != null) {
//                    Log.e("lunchCollect", "listen failed", error);
//                }
//            }
//        });
//    }

    public LunchRestaurant getLunchRestaurant() {
        getLunchRestaurantCollection().document(getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mLunchRestaurant = documentSnapshot.toObject(LunchRestaurant.class);
                }
            }
        });

        return mLunchRestaurant;
    }

}
