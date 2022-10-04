package com.hangyeollee.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.utility.MyCalendar;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private MutableLiveData<List<User>> mUserListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LunchRestaurant>> mLunchRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LikedRestaurant>> mLikedRestaurantMutableLiveData = new MutableLiveData<>();

    public FirebaseRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        mAuth = firebaseAuth;
        mFirestore = firestore;
    }

    // -----------FirebaseAuth method starts----------- //
    public FirebaseAuth getFirebaseInstance() {
        return mAuth;
    }


    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void updateUserName(String newName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newName).build();
        getCurrentUser().updateProfile(profileUpdates);
    }

    public void updateUserPhoto(String photoUri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(photoUri)).build();
        getCurrentUser().updateProfile(profileUpdates);
    }

    public void deleteUserFromFirebaseAuth() {
        getCurrentUser().delete();
    }

    public void signOutFromFirebaseAuth() {
        mAuth.signOut();
    }

    // -----------Firestore method starts----------- //

    public CollectionReference getUsersCollection() {
        return mFirestore.collection("users");
    }

    public CollectionReference getDateCollection() {
        return mFirestore.collection(MyCalendar.getCurrentDate());
    }

    public CollectionReference getLikedRestaurantCollection() {
        return mFirestore.collection("likedRestaurant");
    }

    public void saveUserInFirestore() {
        String id = getCurrentUser().getUid();
        String photoUrl = getCurrentUser().getPhotoUrl().toString();
        String username = getCurrentUser().getDisplayName();

        User userToCreate = new User(id, username, photoUrl);

        getUsersCollection().document(getCurrentUser().getUid()).set(userToCreate, SetOptions.mergeFields("id", "name", "photoUrl")).addOnSuccessListener(v -> {
            Log.e("Firestore", "user successfully stored !");
        }).addOnFailureListener(v -> {
            Log.e("Firestore", "user saving failed");
        });
    }

    public void saveLunchRestaurant(LunchRestaurant lunchRestaurant) {
        getDateCollection().document(getCurrentUser().getUid()).set(lunchRestaurant, SetOptions.merge());
    }

    public MutableLiveData<List<User>> getUsersList() {
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

    public MutableLiveData<List<LunchRestaurant>> getLunchRestaurantListOfAllUsers() {
        getDateCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("lunchRestauCollection", "Listen failed.", error);
                    mLunchRestaurantMutableLiveData.postValue(null);
                    return;
                }
                List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    LunchRestaurant lunchRestaurant = document.toObject(LunchRestaurant.class);
                    lunchRestaurantList.add(lunchRestaurant);
                }
                mLunchRestaurantMutableLiveData.setValue(lunchRestaurantList);
            }
        });
        return mLunchRestaurantMutableLiveData;
    }

    public void setLikeRestaurant(LikedRestaurant likedRestaurant) {
        getUsersCollection().document(getCurrentUser().getUid()).update("likedRestaurantList", FieldValue.arrayUnion(likedRestaurant));
    }

    public MutableLiveData<List<LikedRestaurant>> getLikedRestaurantList() {
        getUsersCollection().document(getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("likedRestauCollection", "Listen failed.", error);
                    mLikedRestaurantMutableLiveData.postValue(null);
                    return;
                }
                assert documentSnapshot != null;

                User user = documentSnapshot.toObject(User.class);
                List<LikedRestaurant> likedRestaurantList = user.getLikedRestaurantList();
                mLikedRestaurantMutableLiveData.setValue(likedRestaurantList);
            }
        });
        return mLikedRestaurantMutableLiveData;
    }

}
