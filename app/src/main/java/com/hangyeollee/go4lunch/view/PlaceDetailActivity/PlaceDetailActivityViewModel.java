package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
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

    private final MediatorLiveData<PlaceDetailActivityViewState> mediatorLiveData = new MediatorLiveData<>();

    public PlaceDetailActivityViewModel(Application context, PlaceDetailDataRepository placeDetailDataRepository, FirebaseRepository firebaseRepository) {
        this.context = context;
        this.placeDetailDataRepository = placeDetailDataRepository;
        this.firebaseRepository = firebaseRepository;

        mediatorLiveData.addSource(placeDetailDataRepository.getPlaceDetailLiveData(), placeDetailData ->
                combine(
                        placeDetailData,
                        this.firebaseRepository.getUsersList().getValue(),
                        this.firebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                        this.firebaseRepository.getLikedRestaurantList().getValue())
        );

        mediatorLiveData.addSource(this.firebaseRepository.getUsersList(), userList ->
                combine(
                        this.placeDetailDataRepository.getPlaceDetailLiveData().getValue(),
                        userList,
                        this.firebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                        this.firebaseRepository.getLikedRestaurantList().getValue()
                ));

        mediatorLiveData.addSource(this.firebaseRepository.getLunchRestaurantListOfAllUsers(), lunchRestaurantList ->
                combine(
                        this.placeDetailDataRepository.getPlaceDetailLiveData().getValue(),
                        this.firebaseRepository.getUsersList().getValue(),
                        lunchRestaurantList,
                        this.firebaseRepository.getLikedRestaurantList().getValue())
        );

        mediatorLiveData.addSource(this.firebaseRepository.getLikedRestaurantList(), likedRestaurantList ->
                combine(
                        this.placeDetailDataRepository.getPlaceDetailLiveData().getValue(),
                        this.firebaseRepository.getUsersList().getValue(),
                        this.firebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                        likedRestaurantList)
        );

    }

    private void combine(MyPlaceDetailData myPlaceDetailData, List<User> userList, List<LunchRestaurant> lunchRestaurantList, List<LikedRestaurant> likedRestaurantList) {
        if (myPlaceDetailData == null || lunchRestaurantList == null || likedRestaurantList == null) {
            return;
        }

        String website = null;
        String internationalPhoneNumber = null;

        if (myPlaceDetailData.getResult().getWebsite() == null) {
            context.getString(R.string.website_unavailable);
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

        boolean isSelectedAsLunchRestaurant = false;
        boolean isSelectedAsLikedRestaurant = false;

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            isSelectedAsLunchRestaurant = myPlaceDetailData.getResult().getName().equalsIgnoreCase(lunchRestaurant.getRestaurantMame());
        }

        for (LikedRestaurant likedRestaurant : likedRestaurantList) {
            isSelectedAsLikedRestaurant = myPlaceDetailData.getResult().getName().equalsIgnoreCase(likedRestaurant.getName());
        }

        PlaceDetailActivityViewState activityViewState = new PlaceDetailActivityViewState(
                myPlaceDetailData.getResult().getPhotos().get(0).getPhotoReference(),
                myPlaceDetailData.getResult().getName(),
                myPlaceDetailData.getResult().getVicinity(),
                myPlaceDetailData.getResult().getRating().floatValue(),
                internationalPhoneNumber,
                website,
                recyclerViewItemViewStateList,
                isSelectedAsLikedRestaurant,
                isSelectedAsLunchRestaurant
        );

        mediatorLiveData.setValue(activityViewState);

    }

    public LiveData<PlaceDetailActivityViewState> getPlaceDetailActivityViewStateLiveData() {
        return mediatorLiveData;
    }

    public void requestPlaceDetailData(String placeId) {
        placeDetailDataRepository.fetchData(placeId);
    }

    public FirebaseUser getCurrentUser() {
        return firebaseRepository.getCurrentUser();
    }

    public void onSetLunchRestaurantButtonClicked(LunchRestaurant lunchRestaurant) {
        firebaseRepository.saveLunchRestaurant(lunchRestaurant);
    }

    public void onSetLikedRestaurantButtonClicked(LikedRestaurant likedRestaurant) {
        firebaseRepository.setLikeRestaurant(likedRestaurant);
    }



}