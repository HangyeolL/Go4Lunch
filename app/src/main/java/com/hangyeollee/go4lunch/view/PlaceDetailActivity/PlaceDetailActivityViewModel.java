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

import java.util.List;

public class PlaceDetailActivityViewModel extends ViewModel {

    private Application context;
    private PlaceDetailDataRepository placeDetailDataRepository;
    private FirebaseRepository firebaseRepository;

    private MediatorLiveData<PlaceDetailActivityViewState> mediatorLiveData = new MediatorLiveData<>();

    public PlaceDetailActivityViewModel(Application context,  PlaceDetailDataRepository placeDetailDataRepository, FirebaseRepository firebaseRepository) {
        this.context = context;
        this.placeDetailDataRepository = placeDetailDataRepository;
        this.firebaseRepository = firebaseRepository;

        mediatorLiveData.addSource(placeDetailDataRepository.getPlaceDetailLiveData(), placeDetailData -> {
            combine(
                    placeDetailData,
                    this.firebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                    this.firebaseRepository.getLikedRestaurantList().getValue());
        });

        mediatorLiveData.addSource(this.firebaseRepository.getLunchRestaurantListOfAllUsers(), lunchRestaurantList -> {
            combine(
                    placeDetailDataRepository.getPlaceDetailLiveData().getValue(),
                    lunchRestaurantList,
                    this.firebaseRepository.getLikedRestaurantList().getValue());
        });

        mediatorLiveData.addSource(this.firebaseRepository.getLikedRestaurantList(), likedRestaurantList -> {
            combine(
                    placeDetailDataRepository.getPlaceDetailLiveData().getValue(),
                    this.firebaseRepository.getLunchRestaurantListOfAllUsers().getValue(),
                    likedRestaurantList
            );
        });
    }

    private void combine(MyPlaceDetailData myPlaceDetailData, List<LunchRestaurant> lunchRestaurantList, List<LikedRestaurant> likedRestaurantList) {
        if (myPlaceDetailData == null || lunchRestaurantList == null || likedRestaurantList == null) {
            return;
        }

        String website;
        String internationalPhoneNumber;

        if (myPlaceDetailData.getResult().getWebsite() == null) {
            context.getString(R.string.website_unavailable);
        } else {
            website = myPlaceDetailData.getResult().getWebsite();
        }
        
        if(myPlaceDetailData.getResult().getInternationalPhoneNumber() == null) {
            context.getString(R.string.international_phone_number_unavailable);
        } else {
            internationalPhoneNumber = myPlaceDetailData.getResult().getInternationalPhoneNumber();
        }

        PlaceDetailActivityViewState activityViewState = new PlaceDetailActivityViewState(
                myPlaceDetailData.getResult().getPhotos().get(0).getPhotoReference(),
                myPlaceDetailData.getResult().getName(),
                myPlaceDetailData.getResult().getVicinity(),
                myPlaceDetailData.getResult().getRating().floatValue(),
                internationalPhoneNumber,
                website,
                
        )

    }


    // -----------Firebase method starts----------- //

    public FirebaseUser getCurrentUser() {
        return firebaseRepository.getCurrentUser();
    }

    public void setLunchRestaurant(LunchRestaurant lunchRestaurant) {
        firebaseRepository.saveLunchRestaurant(lunchRestaurant);
    }


    public void setLikedRestaurant(LikedRestaurant likedRestaurant) {
        firebaseRepository.setLikeRestaurant(likedRestaurant);
    }

    public LiveData<PlaceDetailActivityViewState> getMediatorLiveData() {
        return mediatorLiveData;
    }

}