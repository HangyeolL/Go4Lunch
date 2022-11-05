package com.hangyeollee.go4lunch.view.MainHomeActivity;

import static com.hangyeollee.go4lunch.utils.resourceToUri.resourceToUri;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;
import com.hangyeollee.go4lunch.view.LogInActivity.LogInActivity;
import com.hangyeollee.go4lunch.view.SettingsActivity.SettingsActivity;

import java.util.List;

public class MainHomeActivityViewModel extends ViewModel {

    private final Application context;
    private final FirebaseRepository firebaseRepository;
    private final LocationRepository locationRepository;
    private final AutoCompleteDataRepository autoCompleteDataRepository;

    private final MediatorLiveData<MainHomeActivityViewState> mainHomeActivityViewStateMediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<String> yourLunchToastMessageSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Intent> settingsIntentSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Intent> logOutIntentSingleLiveEvent = new SingleLiveEvent<>();

    public MainHomeActivityViewModel(
            Application context,
            FirebaseRepository firebaseRepository,
            LocationRepository locationRepository,
            AutoCompleteDataRepository autoCompleteDataRepository
    ) {
        this.context = context;
        this.firebaseRepository = firebaseRepository;
        this.locationRepository = locationRepository;
        this.autoCompleteDataRepository = autoCompleteDataRepository;

        LiveData<Location> locationLiveData = this.locationRepository.getLocationLiveData();
        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = this.firebaseRepository.getLunchRestaurantListOfAllUsers();

        mainHomeActivityViewStateMediatorLiveData.addSource(locationLiveData,
                location -> combine(location, lunchRestaurantListLiveData.getValue())
        );
    }

    private void combine(Location location, List<LunchRestaurant> lunchRestaurantList) {
        if (location == null) {
            return;
        }

        String providerId;
        String userName;
        String userEmail;
        String userPhotoUrl;
        String lunchRestaurantName = null;

        UserInfo firebaseUserInfo = firebaseRepository.getCurrentUser().getProviderData().get(1);

        providerId = firebaseUserInfo.getProviderId();
        userName = firebaseUserInfo.getDisplayName();

        if (firebaseUserInfo.getEmail() == null) {
            userEmail = context.getString(R.string.email_unavailable);
        } else {
            userEmail = firebaseUserInfo.getEmail();
        }

        if (firebaseUserInfo.getPhotoUrl() == null) {
            userPhotoUrl = resourceToUri(context, R.drawable.ic_baseline_person_outline_24);
        } else {
            userPhotoUrl = firebaseUserInfo.getPhotoUrl().toString();
        }

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            if (firebaseRepository.getCurrentUser().getUid().equals(lunchRestaurant.getUserId())) {
                lunchRestaurantName = lunchRestaurant.getRestaurantName();
            }
        }

        MainHomeActivityViewState mainHomeActivityViewState = new MainHomeActivityViewState(
                providerId, userName, userEmail, userPhotoUrl, lunchRestaurantName);

        mainHomeActivityViewStateMediatorLiveData.setValue(mainHomeActivityViewState);
    }

    /** GETTERS */

    public LiveData<MainHomeActivityViewState> getMainHomeActivityViewStateLiveData() {
        return mainHomeActivityViewStateMediatorLiveData;
    }

    public SingleLiveEvent<String> getYourLunchToastMessageSingleLiveEvent() {
        return yourLunchToastMessageSingleLiveEvent;
    }

    public SingleLiveEvent<Intent> getSettingsIntentSingleLiveEvent() {
        return settingsIntentSingleLiveEvent;
    }

    public SingleLiveEvent<Intent> getLogOutIntentSingleLiveEvent() {
        return logOutIntentSingleLiveEvent;
    }

    public void onYourLunchClicked(MainHomeActivityViewState mainHomeActivityViewState) {
        if (mainHomeActivityViewState.getLunchRestaurantName() == null) {
            yourLunchToastMessageSingleLiveEvent.setValue(context.getString(R.string.didnt_decide_where_to_lunch));
        } else {
            yourLunchToastMessageSingleLiveEvent.setValue(mainHomeActivityViewState.getLunchRestaurantName());
        }
    }

    /** EVENTS */

    public void onSettingsClicked() {
        settingsIntentSingleLiveEvent.setValue(new Intent(context, SettingsActivity.class));
    }

    public void onLogOutClicked() {
        firebaseRepository.signOutFromFirebaseAuth();
        logOutIntentSingleLiveEvent.setValue(new Intent(context, LogInActivity.class));
    }

    public void onSearchViewTextChanged(String searchViewText) {
        autoCompleteDataRepository.setUserSearchTextQuery(searchViewText);
    }

    /** Location Repository */

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
    }


}