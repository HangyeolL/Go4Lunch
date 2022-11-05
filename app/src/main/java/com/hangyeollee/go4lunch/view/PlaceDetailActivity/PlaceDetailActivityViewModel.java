package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import static com.hangyeollee.go4lunch.utils.resourceToUri.resourceToUri;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utils.MyCalendar;
import com.hangyeollee.go4lunch.utils.MySharedPreferenceUtil;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivityViewModel extends ViewModel {

    private final Application context;
    private final FirebaseRepository firebaseRepository;

    private final MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();

    private final MediatorLiveData<PlaceDetailActivityViewState> mediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Intent> intentSingleLiveEvent = new SingleLiveEvent<>();

    public PlaceDetailActivityViewModel(Application context, PlaceDetailDataRepository placeDetailDataRepository, FirebaseRepository firebaseRepository) {
        this.context = context;
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
                myPlaceDetailData.getResult().getInternationalPhoneNumber(),
                myPlaceDetailData.getResult().getWebsite(),
                recyclerViewItemViewStateList,
                isSelectedAsLikedRestaurant ? R.color.orange : R.color.blue,
                isSelectedAsLunchRestaurant ? ResourcesCompat.getColor(context.getResources(), R.color.orange, null) : ResourcesCompat.getColor(context.getResources(), R.color.blue, null),
                isSelectedAsLikedRestaurant,
                isSelectedAsLunchRestaurant
        );

        mediatorLiveData.setValue(activityViewState);

    }

    public LiveData<PlaceDetailActivityViewState> getPlaceDetailActivityViewStateLiveData() {
        return mediatorLiveData;
    }

    public SingleLiveEvent<String> getToastMessageSingleLiveEvent() {
        return toastMessageSingleLiveEvent;
    }

    public SingleLiveEvent<Intent> getIntentSingleLiveEvent() {
        return intentSingleLiveEvent;
    }

    public void onPlaceIdFetched(String placeId) {
        Log.i("onPlaceIdFetched", placeId);
        placeIdMutableLiveData.setValue(placeId);
    }

    public void onButtonCallClicked(PlaceDetailActivityViewState placeDetailActivityViewState) {
        if (placeDetailActivityViewState.getInternationalPhoneNumber() == null) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.international_phone_number_unavailable));
        } else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + placeDetailActivityViewState.getInternationalPhoneNumber()));
            intentSingleLiveEvent.setValue(callIntent);
        }
    }

    public void onButtonWebsiteClicked(PlaceDetailActivityViewState placeDetailActivityViewState) {
        if (placeDetailActivityViewState.getWebsite() == null) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.website_unavailable));
        } else {
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
            websiteIntent.setData(Uri.parse(placeDetailActivityViewState.getWebsite()));
            intentSingleLiveEvent.setValue(websiteIntent);
        }
    }

    public void onButtonLikeClicked(PlaceDetailActivityViewState placeDetailActivityViewState) {
        LikedRestaurant likedRestaurant = new LikedRestaurant(placeIdMutableLiveData.getValue(), placeDetailActivityViewState.getName());
        firebaseRepository.addOrRemoveLikedRestaurant(likedRestaurant);

        if (placeDetailActivityViewState.isSelectedAsLikedRestaurant()) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.add_to_liked_restaurant_list));
        } else {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.removed_from_the_liked_restaurant_list));
        }
    }

    public void onFloatingActionButtonClicked(PlaceDetailActivityViewState placeDetailActivityViewState) {
        if (placeDetailActivityViewState.isSelectedAsLunchRestaurant()) {
            toastMessageSingleLiveEvent.setValue(
                    context.getString(R.string.you_already_decided_to_go)
                            + placeDetailActivityViewState.getName()
            );
        } else {
            toastMessageSingleLiveEvent.setValue(
                    context.getString(R.string.you_will_go_to)
                            + placeDetailActivityViewState.getName()
                            + context.getString(R.string.for_lunch)
            );

            LunchRestaurant lunchRestaurant = new LunchRestaurant(
                    placeIdMutableLiveData.getValue(),
                    firebaseRepository.getCurrentUser().getUid(),
                    placeDetailActivityViewState.getName(),
                    MyCalendar.getCurrentDate()
            );

            firebaseRepository.addOrRemoveLunchRestaurant(lunchRestaurant);

            SharedPreferences.Editor mSharedPrefEditor = new MySharedPreferenceUtil(context).getInstanceOfEditor();
            mSharedPrefEditor.putString("LunchRestaurant", lunchRestaurant.getRestaurantName());
            mSharedPrefEditor.commit();
        }
    }
}
