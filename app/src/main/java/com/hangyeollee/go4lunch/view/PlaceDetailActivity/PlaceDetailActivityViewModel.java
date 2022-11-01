package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivityViewModel extends ViewModel {

    private final Application context;
    private final PlaceDetailDataRepository placeDetailDataRepository;
    private final FirebaseRepository firebaseRepository;

    private final MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();

    private final MediatorLiveData<PlaceDetailActivityViewState> mediatorLiveData = new MediatorLiveData<>();

    public PlaceDetailActivityViewModel(Application context, PlaceDetailDataRepository placeDetailDataRepository, FirebaseRepository firebaseRepository) {
        this.context = context;
        this.placeDetailDataRepository = placeDetailDataRepository;
        this.firebaseRepository = firebaseRepository;

        @SuppressWarnings("Convert2MethodRef")
        LiveData<MyPlaceDetailData> placeDetailLiveData = Transformations.switchMap(
            placeIdMutableLiveData,
            placeId -> placeDetailDataRepository.getPlaceDetailLiveData(placeId)
        );
        LiveData<List<User>> userListLiveData = firebaseRepository.getUsersList();
        LiveData<List<LunchRestaurant>> lunchRestaurantListOfAllUsersLiveData = firebaseRepository.getLunchRestaurantListOfAllUsers();
        LiveData<List<LikedRestaurant>> likedRestaurantListLiveData = firebaseRepository.getLikedRestaurantList();

        mediatorLiveData.addSource(placeDetailLiveData, placeDetailData ->
            combine(
                placeDetailData,
                userListLiveData.getValue(),
                lunchRestaurantListOfAllUsersLiveData.getValue(),
                likedRestaurantListLiveData.getValue()
            )
        );

        mediatorLiveData.addSource(userListLiveData, userList ->
            combine(
                placeDetailLiveData.getValue(),
                userList,
                lunchRestaurantListOfAllUsersLiveData.getValue(),
                likedRestaurantListLiveData.getValue()
            )
        );

        mediatorLiveData.addSource(lunchRestaurantListOfAllUsersLiveData, lunchRestaurantList ->
            combine(
                placeDetailLiveData.getValue(),
                userListLiveData.getValue(),
                lunchRestaurantList,
                likedRestaurantListLiveData.getValue()
            )
        );

        mediatorLiveData.addSource(likedRestaurantListLiveData, likedRestaurantList ->
            combine(
                placeDetailLiveData.getValue(),
                userListLiveData.getValue(),
                lunchRestaurantListOfAllUsersLiveData.getValue(),
                likedRestaurantList
            )
        );

    }

    private void combine(
        @Nullable MyPlaceDetailData myPlaceDetailData,
        @Nullable List<User> userList,
        @Nullable List<LunchRestaurant> lunchRestaurantList,
        @Nullable List<LikedRestaurant> likedRestaurantList
    ) {
        if (myPlaceDetailData == null || lunchRestaurantList == null || likedRestaurantList == null) {
            return;
        }

        String website = null;
        String internationalPhoneNumber = null;
        String photoUrl;
        float rating;

        if (myPlaceDetailData.getResult().getPhotos() != null) {
            String photoReference = myPlaceDetailData.getResult().getPhotos().get(0).getPhotoReference();

            photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + photoReference
                + "&key="
                + BuildConfig.PLACES_API_KEY;
        } else {
            photoUrl = resourceToUri(context, R.drawable.ic_baseline_local_dining_24);
        }

        if (myPlaceDetailData.getResult().getRating() != null) {
            rating = myPlaceDetailData.getResult().getRating().floatValue();
        } else {
            rating = 0;
        }

        if (myPlaceDetailData.getResult().getWebsite() == null) {
            website = context.getString(R.string.website_unavailable);
        } else {
            website = myPlaceDetailData.getResult().getWebsite();
        }

        if (myPlaceDetailData.getResult().getInternationalPhoneNumber() == null) {
            context.getString(R.string.international_phone_number_unavailable);
        } else {
            internationalPhoneNumber = myPlaceDetailData.getResult().getInternationalPhoneNumber();
        }


        List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            if (userList != null) {
                for (User user : userList) {
                    if (user.getId().equalsIgnoreCase(lunchRestaurant.getUserId())) {
                        recyclerViewItemViewStateList.add(
                            new PlaceDetailActivityRecyclerViewItemViewState(
                                user.getName(),
                                user.getPhotoUrl())
                        );
                    }
                }
            }
        }

        boolean isSelectedAsLunchRestaurant = false;
        boolean isSelectedAsLikedRestaurant = false;

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            isSelectedAsLunchRestaurant = myPlaceDetailData.getResult().getName().equalsIgnoreCase(lunchRestaurant.getRestaurantName());
        }

        for (LikedRestaurant likedRestaurant : likedRestaurantList) {
            isSelectedAsLikedRestaurant = myPlaceDetailData.getResult().getName().equalsIgnoreCase(likedRestaurant.getName());
        }

        PlaceDetailActivityViewState activityViewState = new PlaceDetailActivityViewState(
            photoUrl,
            myPlaceDetailData.getResult().getName(),
            myPlaceDetailData.getResult().getVicinity(),
            rating,
            internationalPhoneNumber,
            website,
            recyclerViewItemViewStateList,
            isSelectedAsLikedRestaurant ? R.color.orange : R.color.white,
            isSelectedAsLunchRestaurant
        );

        mediatorLiveData.setValue(activityViewState);

    }

    public LiveData<PlaceDetailActivityViewState> getPlaceDetailActivityViewStateLiveData() {
        return mediatorLiveData;
    }

    public void onPlaceIdFetched(String placeId) {
        Log.i("onPlaceIdFetched", placeId);
        placeIdMutableLiveData.setValue(placeId);
    }

    public FirebaseUser getCurrentUser() {
        return firebaseRepository.getCurrentUser();
    }

    public void onSetLunchRestaurantButtonClicked(LunchRestaurant lunchRestaurant) {
        firebaseRepository.saveOrRemoveLunchRestaurant(lunchRestaurant);
    }

    public void onLikedRestaurantButtonClicked(LikedRestaurant likedRestaurant) {
        firebaseRepository.addOrRemoveLikedRestaurant(likedRestaurant);
    }


    private static String resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID))
            .toString();
    }

    public void onButtonCallClicked(PlaceDetailActivityViewState placeDetailActivityViewState) {
        if (placeDetailActivityViewState.getInternationalPhoneNumber() == null) {
            viewActionToast.setValue("blabla no phone");
        } else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + placeDetailActivityViewState.getInternationalPhoneNumber()));
            viewActionIntent.setValue(callIntent);
        }
    }
}