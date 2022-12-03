package com.hangyeollee.go4lunch.ui.place_detail_activity;

import static com.hangyeollee.go4lunch.utils.UtilBox.resourceToUri;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LikedRestaurant;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.User;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailViewModel extends ViewModel {

    private final Application context;
    private final FirebaseRepository firebaseRepository;

    private final MutableLiveData<String> placeIdMutableLiveData = new MutableLiveData<>();

    private final MediatorLiveData<PlaceDetailViewState> mediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Intent> intentSingleLiveEvent = new SingleLiveEvent<>();

    public PlaceDetailViewModel(Application context, PlaceDetailDataRepository placeDetailDataRepository, FirebaseRepository firebaseRepository) {
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

        List<PlaceDetailItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            if (userList != null) {
                for (User user : userList) {
                    if (user.getId().equalsIgnoreCase(lunchRestaurant.getUserId())
                            && lunchRestaurant.getRestaurantName().equalsIgnoreCase(myPlaceDetailData.getResult().getName())) {
                        recyclerViewItemViewStateList.add(
                                new PlaceDetailItemViewState(
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
            if (myPlaceDetailData.getResult().getName().equalsIgnoreCase(lunchRestaurant.getRestaurantName())) {
                isSelectedAsLunchRestaurant = true;
                break;
            }
        }

        for (LikedRestaurant likedRestaurant : likedRestaurantList) {
            if (myPlaceDetailData.getResult().getName().equalsIgnoreCase(likedRestaurant.getName())) {
                isSelectedAsLikedRestaurant = true;
                break;
            }
        }

        PlaceDetailViewState activityViewState = new PlaceDetailViewState(
                photoUrl,
                myPlaceDetailData.getResult().getName(),
                myPlaceDetailData.getResult().getVicinity(),
                rating,
                myPlaceDetailData.getResult().getInternationalPhoneNumber(),
                myPlaceDetailData.getResult().getWebsite(),
                recyclerViewItemViewStateList,
                isSelectedAsLikedRestaurant ? context.getColor(R.color.yellow) : context.getColor(R.color.grey),
                isSelectedAsLunchRestaurant ? R.color.orange : R.color.grey,
                isSelectedAsLikedRestaurant,
                isSelectedAsLunchRestaurant
        );

        mediatorLiveData.setValue(activityViewState);

    }

    public LiveData<PlaceDetailViewState> getPlaceDetailActivityViewStateLiveData() {
        return mediatorLiveData;
    }

    public SingleLiveEvent<String> getToastMessageSingleLiveEvent() {
        return toastMessageSingleLiveEvent;
    }

    public SingleLiveEvent<Intent> getIntentSingleLiveEvent() {
        return intentSingleLiveEvent;
    }

    public void onPlaceIdFetched(String placeId) {
//        Log.i("onPlaceIdFetched", placeId);
        placeIdMutableLiveData.setValue(placeId);
    }

    public void onButtonCallClicked(PlaceDetailViewState placeDetailViewState) {
        if (placeDetailViewState.getInternationalPhoneNumber() == null) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.international_phone_number_unavailable));
        } else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + placeDetailViewState.getInternationalPhoneNumber()));
            intentSingleLiveEvent.setValue(callIntent);
        }
    }

    public void onButtonWebsiteClicked(PlaceDetailViewState placeDetailViewState) {
        if (placeDetailViewState.getWebsite() == null) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.website_unavailable));
        } else {
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
            websiteIntent.setData(Uri.parse(placeDetailViewState.getWebsite()));
            intentSingleLiveEvent.setValue(websiteIntent);
        }
    }

    public void onButtonLikeClicked(PlaceDetailViewState placeDetailViewState) {
        firebaseRepository.addOrRemoveLikedRestaurant(
                placeIdMutableLiveData.getValue(),
                placeDetailViewState.getName(),
                placeDetailViewState.isSelectedAsLikedRestaurant()
        );

        if (placeDetailViewState.isSelectedAsLikedRestaurant()) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.removed_from_the_liked_restaurant_list));
        } else {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.add_to_liked_restaurant_list));
        }
    }

    public void onFloatingActionButtonClicked(PlaceDetailViewState placeDetailViewState) {
        firebaseRepository.addOrRemoveLunchRestaurant(
                placeIdMutableLiveData.getValue(),
                firebaseRepository.getCurrentUser().getUid(),
                placeDetailViewState.getName(),
                LocalDate.now().toString(),
                placeDetailViewState.isSelectedAsLunchRestaurant()
        );

        if (placeDetailViewState.isSelectedAsLunchRestaurant()) {
            toastMessageSingleLiveEvent.setValue(
                    context.getString(R.string.you_will_not_go_to)
                            + placeDetailViewState.getName()
            );
        } else {
            toastMessageSingleLiveEvent.setValue(
                    context.getString(R.string.you_will_go_to)
                            + placeDetailViewState.getName()
                            + context.getString(R.string.for_lunch)
            );

        }
    }
}
