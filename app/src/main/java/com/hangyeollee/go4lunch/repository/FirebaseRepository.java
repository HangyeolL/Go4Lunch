package com.hangyeollee.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.utils.MyCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestoreDatabase;

    private final MutableLiveData<List<User>> userListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LikedRestaurant>> likedRestaurantMutableLiveData = new MutableLiveData<>();

    public FirebaseRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestoreDatabase) {
        this.firebaseAuth = firebaseAuth;
        this.firestoreDatabase = firestoreDatabase;
    }

    // -----------FirebaseAuth method starts----------- //
    public FirebaseAuth getFirebaseInstance() {
        return firebaseAuth;
    }


    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
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
        firebaseAuth.signOut();
    }

    // -----------Firestore method starts----------- //

    public CollectionReference getUsersCollection() {
        return firestoreDatabase.collection("users");
    }

    public CollectionReference getDateCollection() {
        return firestoreDatabase.collection(MyCalendar.getCurrentDate());
    }

    public CollectionReference getLikedRestaurantCollection() {
        return firestoreDatabase.collection("likedRestaurant");
    }

    public void saveUserInFirestore() {
        String id = getCurrentUser().getUid();
        String photoUrl = Objects.requireNonNull(getCurrentUser().getPhotoUrl()).toString();
        String username = getCurrentUser().getDisplayName();

        User userToCreate = new User(id, username, photoUrl, new ArrayList<>());

        //TODO not merge likedRestaurantList!!
        getUsersCollection().document(getCurrentUser().getUid()).set(userToCreate, SetOptions.mergeFields(id, username))
                .addOnSuccessListener(v ->
                        Log.e("Firestore", "user successfully stored !")).
                addOnFailureListener(v ->
                        Log.e("Firestore", "user saving failed")
                );
    }

    public LiveData<List<User>> getUsersList() {
        getUsersCollection().addSnapshotListener(
                (querySnapshot, error) -> {
                    if (error != null) {
                        Log.w("userCollection", "Listen failed.", error);
                        userListMutableLiveData.postValue(null);
                        return;
                    }
                    List<User> userList = new ArrayList<>();
                    assert querySnapshot != null;
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                    userListMutableLiveData.setValue(userList);
                }
        );
        return userListMutableLiveData;
    }

    public LiveData<List<LunchRestaurant>> getLunchRestaurantListOfAllUsers() {
        getDateCollection().addSnapshotListener(
                (querySnapshot, error) -> {
                    if (error != null) {
                        Log.w("lunchRestauCollection", "Listen failed.", error);
                        lunchRestaurantMutableLiveData.postValue(null);
                        return;
                    }
                    List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
                    assert querySnapshot != null;
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        LunchRestaurant lunchRestaurant = document.toObject(LunchRestaurant.class);
                        lunchRestaurantList.add(lunchRestaurant);
                    }
                    lunchRestaurantMutableLiveData.setValue(lunchRestaurantList);
                }
        );
        return lunchRestaurantMutableLiveData;
    }

    public LiveData<List<LikedRestaurant>> getLikedRestaurantList() {
        getUsersCollection().document(getCurrentUser().getUid()).addSnapshotListener(
                (documentSnapshot, error) -> {
                    if (error != null) {
                        Log.w("likedRestauCollection", "Listen failed.", error);
                        likedRestaurantMutableLiveData.postValue(null);
                        return;
                    }

                    assert documentSnapshot != null;

                    User user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    List<LikedRestaurant> likedRestaurantList = user.getLikedRestaurantList();
                    likedRestaurantMutableLiveData.setValue(likedRestaurantList);
                }
        );
        return likedRestaurantMutableLiveData;
    }

    public void saveOrRemoveLunchRestaurant(LunchRestaurant lunchRestaurant) {
//        firestoreDatabase.runTransaction()
        getDateCollection().document(getCurrentUser().getUid()).set(lunchRestaurant, SetOptions.merge());
    }

    public void addOrRemoveLikedRestaurant(LikedRestaurant likedRestaurant) {
        DocumentReference docRef = getUsersCollection().document(getCurrentUser().getUid());

        firestoreDatabase.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentSnapshot documentSnapshot = transaction.get(docRef);
                    User user = documentSnapshot.toObject(User.class);
                    assert user != null;

                    List<LikedRestaurant> likedRestaurantList = user.getLikedRestaurantList();

                    if (likedRestaurantList.isEmpty() &&
                    !likedRestaurantList.contains(likedRestaurant)) {
//                        likedRestaurantList.add(likedRestaurant);
//                        transaction.update(
//                                docRef,
//                                "likedRestaurantList",
//                                likedRestaurantList
//                        );
                        docRef.update("likedRestaurantList", FieldValue.arrayUnion(likedRestaurant));

                    } else {
                        for (LikedRestaurant restaurant : likedRestaurantList) {
                            if (restaurant.getId().equalsIgnoreCase(likedRestaurant.getId())) {
//                                likedRestaurantList.remove(likedRestaurant);
                                docRef.update("likedRestaurantList", FieldValue.arrayRemove(likedRestaurant));

                            } else {
//                                likedRestaurantList.add(likedRestaurant);
                                docRef.update("likedRestaurantList", FieldValue.arrayUnion(likedRestaurant));
                            }
//                            transaction.update(
//                                    docRef,
//                                    "likedRestaurantList",
//                                    likedRestaurantList
//                            );
                        }
                    }

                    return null;
                }
        ).addOnFailureListener(failure -> Log.w("likedRestaurant", failure.getMessage())
        ).addOnSuccessListener(success -> Log.d("likedRestaurant", "Transaction success!"));
    }


}
