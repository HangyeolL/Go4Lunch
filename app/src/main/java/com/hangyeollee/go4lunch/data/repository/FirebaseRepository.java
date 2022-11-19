package com.hangyeollee.go4lunch.data.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hangyeollee.go4lunch.data.model.LikedRestaurant;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOutFromFirebaseAuth() {
        firebaseAuth.signOut();
    }

    // -----------Firestore method starts----------- //

    public CollectionReference getUsersCollection() {
        return firestoreDatabase.collection("users");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CollectionReference getDateCollection() {
        return firestoreDatabase.collection(LocalDate.now().toString());
    }

    public void saveUserInFirestore() {
        String id = getCurrentUser().getUid();
        String photoUrl = getCurrentUser().getPhotoUrl().toString();
        String username = getCurrentUser().getDisplayName();

        User userToCreate = new User(id, username, photoUrl, new ArrayList<>());

        getUsersCollection().document(getCurrentUser().getUid()).set(userToCreate, SetOptions.mergeFields("id", "name", "photoUrl"))
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
                        userListMutableLiveData.setValue(null);
                        return;
                    }
                    if (querySnapshot != null) {
                        userListMutableLiveData.setValue(querySnapshot.toObjects(User.class));
                    }
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
        getUsersCollection().document(getCurrentUser().getUid()).collection("liked_restaurants").addSnapshotListener(
                (querySnapshot, error) -> {
                    if (error != null) {
                        Log.w("likedRestauCollection", "Listen failed.", error);
                        likedRestaurantMutableLiveData.postValue(null);
                        return;
                    }

                    if (querySnapshot != null) {
                        List<LikedRestaurant> likedRestaurants = querySnapshot.toObjects(LikedRestaurant.class);
                        likedRestaurantMutableLiveData.setValue(likedRestaurants);
                    }
                }
        );
        return likedRestaurantMutableLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addOrRemoveLunchRestaurant(String restaurantId, String userId, String restaurantName, String date, boolean isSelected) {
       if (isSelected) {
           getDateCollection()
                   .document(getCurrentUser().getUid())
                   .delete()
                   .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           Log.d("Hangyeol", "addOrRemoveLunchRestaurant() called with SUCCESS : " +
                                   "restaurantId = [" + restaurantId + "], " +
                                   "isSelected = [" + isSelected + "]");
                       } else {
                           Log.d("Hangyeol", "addOrRemoveLikedRestaurant() called with FAILURE : " +
                                   "restaurantId = [" + restaurantId + "], " +
                                   "isSelected = [" + isSelected + "]");
                           task.getException().printStackTrace();
                       }
                   });
       } else {
           LunchRestaurant lunchRestaurant = new LunchRestaurant(restaurantId, userId, restaurantName, date);

           getDateCollection()
                   .document(getCurrentUser().getUid())
                   .set(lunchRestaurant)
                   .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           Log.d("Hangyeol", "addOrRemoveLunchRestaurant() called with SUCCESS : " +
                                   "restaurantId = [" + restaurantId + "], " +
                                   "isSelected = [" + isSelected + "]");
                       } else {
                           Log.d("Hangyeol", "addOrRemoveLikedRestaurant() called with FAILURE : " +
                                   "restaurantId = [" + restaurantId + "], " +
                                   "isSelected = [" + isSelected + "]");
                           task.getException().printStackTrace();
                       }
                   });
       }
    }

    public void addOrRemoveLikedRestaurant(String placeId, String name, boolean isLiked) {
        if (isLiked) {
            getUsersCollection()
                    .document(getCurrentUser().getUid())
                    .collection("liked_restaurants")
                    .document(placeId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Hangyeol", "addOrRemoveLikedRestaurant() called with SUCCESS : " +
                                    "placeId = [" + placeId + "], " +
                                    "isLiked = [" + isLiked + "]");
                        } else {
                            Log.d("Hangyeol", "addOrRemoveLikedRestaurant() called with FAILURE : " +
                                    "placeId = [" + placeId + "], " +
                                    "isLiked = [" + isLiked + "]");
                            task.getException().printStackTrace();
                        }
                    });
        } else {
            LikedRestaurant likedRestaurant = new LikedRestaurant(placeId, name);

            getUsersCollection()
                    .document(getCurrentUser().getUid())
                    .collection("liked_restaurants")
                    .document(placeId)
                    .set(likedRestaurant)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Hangyeol", "addOrRemoveLikedRestaurant() called with SUCCESS : " +
                                    "placeId = [" + placeId + "], " +
                                    "isLiked = [" + isLiked + "]");
                        } else {
                            Log.d("Hangyeol", "addOrRemoveLikedRestaurant() called with FAILURE : " +
                                    "placeId = [" + placeId + "], " +
                                    "isLiked = [" + isLiked + "]");
                            task.getException().printStackTrace();
                        }
                    });
        }
    }


}
