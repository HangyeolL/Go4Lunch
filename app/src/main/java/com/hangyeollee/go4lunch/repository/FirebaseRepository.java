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
import com.hangyeollee.go4lunch.utility.MyFirestoreUtil;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private static FirebaseAuth FIREBASEAUTH;
    private FirebaseFirestore mFirestore;

    private MutableLiveData<List<User>> mUserListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LunchRestaurant>> mLunchRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<LikedRestaurant>> mLikedRestaurantMutableLiveData = new MutableLiveData<>();

    private List<LunchRestaurant> mLunchRestaurantListOfSpecificPlace = new ArrayList<>();
    private MutableLiveData<List<User>> mUserListWithLunch = new MutableLiveData<>();

    // Dependency Injection for unit test purpose
    public FirebaseRepository(FirebaseFirestore firestore) {
        mFirestore = firestore;
    }

    // -----------FirebaseAuth method starts----------- //

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

    // -----------Firestore method starts----------- //

    public FirebaseFirestore getFirestoreInstance() {
        return MyFirestoreUtil.getFirestoreInstance();
    }

    public CollectionReference getUsersCollection() {
        return getFirestoreInstance().collection("users");
    }

    public CollectionReference getDateCollection() {
        return getFirestoreInstance().collection(MyCalendar.getCurrentDate());
    }

    public CollectionReference getLikedRestaurantCollection() {
        return getFirestoreInstance().collection("likedRestaurant");
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

    public List<User> getUsersList2() {
        List<User> userList = new ArrayList<>();
        try {
            getUsersCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w("userCollection", "Listen failed.", error);
                        return;
                    }
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                }
            });
        } catch (Exception e) {
            Log.w("Exception", e.getMessage());
        }
        return userList;
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

    public List<LunchRestaurant> getLunchRestaurantListOfAllUsers2() {
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();

        getDateCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("lunchRestauCollection", "Listen failed.", error);
                    return;
                }
                for (QueryDocumentSnapshot document : querySnapshot) {
                    LunchRestaurant lunchRestaurant = document.toObject(LunchRestaurant.class);
                    lunchRestaurantList.add(lunchRestaurant);
                }
            }
        });
        return lunchRestaurantList;
    }

    //    public List<LunchRestaurant> getLunchRestaurantListOfSpecificPlace(String placeId) {
    ////        getDateCollection().whereEqualTo("restaurantId", placeId).addSnapshotListener(new EventListener<QuerySnapshot>() {
    ////            @Override
    ////            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
    ////                if (error != null) {
    ////                    Log.w("UsersListWithLunch", "Listen failed.", error);
    ////                    return;
    ////                }
    ////                for (QueryDocumentSnapshot document : querySnapshot) {
    ////                    LunchRestaurant lunchRestaurant = document.toObject(LunchRestaurant.class);
    ////                    mLunchRestaurantListOfSpecificPlace.add(lunchRestaurant);
    ////                }
    ////            }
    ////        });
    //
    //        getDateCollection().whereEqualTo("restaurantId", placeId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
    //            @Override
    //            public void onSuccess(QuerySnapshot querySnapshot) {
    //                for (QueryDocumentSnapshot document : querySnapshot) {
    //                    LunchRestaurant lunchRestaurant = document.toObject(LunchRestaurant.class);
    //                    mLunchRestaurantListOfSpecificPlace.add(lunchRestaurant);
    //                }
    //            }
    //        });
    //        return mLunchRestaurantListOfSpecificPlace;
    //    }
    //
    //    public MutableLiveData<List<User>> getUserListWithLunch(String placeId) {
    //        List<LunchRestaurant> lunchRestaurantList = getLunchRestaurantListOfSpecificPlace(placeId);
    //
    //        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
    //            getUsersCollection().whereEqualTo("id", lunchRestaurant.getUserId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
    //                @Override
    //                public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
    //                    if (error != null) {
    //                        Log.w("userListWithLunch", "Listen failed.", error);
    //                        mUserListWithLunch.postValue(null);
    //                        return;
    //                    }
    //                    List<User> userList = new ArrayList<>();
    //                    for (QueryDocumentSnapshot document : querySnapshot) {
    //                        User user = document.toObject(User.class);
    //                        userList.add(user);
    //                    }
    //                    mUserListWithLunch.setValue(userList);
    //                }
    //            });
    //        } return mUserListWithLunch;
    //    }

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
