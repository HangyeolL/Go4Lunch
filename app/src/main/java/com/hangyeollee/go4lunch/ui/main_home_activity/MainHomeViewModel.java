package com.hangyeollee.go4lunch.ui.main_home_activity;

import static com.hangyeollee.go4lunch.utils.resourceToUri.resourceToUri;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;
import com.hangyeollee.go4lunch.ui.logIn_activity.LogInActivity;
import com.hangyeollee.go4lunch.ui.settings_activity.SettingsActivity;

import java.util.List;

public class MainHomeViewModel extends ViewModel {

    private final Application context;
    private final FirebaseRepository firebaseRepository;
    private final LocationRepository locationRepository;
    private final AutoCompleteDataRepository autoCompleteDataRepository;

    private final MediatorLiveData<MainHomeViewState> mainHomeActivityViewStateMediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Intent> intentSingleLiveEvent = new SingleLiveEvent<>();

    public MainHomeViewModel(
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

        if (firebaseUserInfo.getEmail() == null || firebaseUserInfo.getEmail().equals("")) {
            userEmail = context.getString(R.string.email_unavailable);
        } else {
            userEmail = firebaseUserInfo.getEmail();
        }

        if (firebaseUserInfo.getPhotoUrl() == null || firebaseUserInfo.getPhotoUrl().toString().isEmpty()) {
            userPhotoUrl = resourceToUri(context, R.drawable.ic_baseline_person_outline_24);
        } else {
            userPhotoUrl = firebaseUserInfo.getPhotoUrl().toString();
        }

        if (lunchRestaurantList != null) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (firebaseRepository.getCurrentUser().getUid().equals(lunchRestaurant.getUserId())) {
                    lunchRestaurantName = lunchRestaurant.getRestaurantName();
                }
            }
        }

        MainHomeViewState mainHomeViewState = new MainHomeViewState(
                providerId, userName, userEmail, userPhotoUrl, lunchRestaurantName);

        mainHomeActivityViewStateMediatorLiveData.setValue(mainHomeViewState);
    }

    /**
     * GETTERS
     */

    public LiveData<MainHomeViewState> getMainHomeActivityViewStateLiveData() {
        return mainHomeActivityViewStateMediatorLiveData;
    }

    public SingleLiveEvent<String> getToastMessageSingleLiveEvent() {
        return toastMessageSingleLiveEvent;
    }

    public SingleLiveEvent<Intent> getIntentSingleLiveEvent() {
        return intentSingleLiveEvent;
    }

    /**
     * EVENTS
     */

    public void onYourLunchClicked(MainHomeViewState mainHomeViewState) {
        if (mainHomeViewState.getLunchRestaurantName() == null) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.did_not_decide_where_to_lunch));
        } else {
            toastMessageSingleLiveEvent.setValue(mainHomeViewState.getLunchRestaurantName());
        }
    }

    public void onSettingsClicked() {
        intentSingleLiveEvent.setValue(new Intent(context, SettingsActivity.class));
    }

    public void onLogOutClicked(String providerId) {
        switch (providerId) {
            case "google.com":
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mSignInClient = GoogleSignIn.getClient(context, gso);
                mSignInClient.signOut();
                break;
            case "facebook.com":
                LoginManager.getInstance().logOut();
                break;
        }

        firebaseRepository.signOutFromFirebaseAuth();
        intentSingleLiveEvent.setValue(new Intent(context, LogInActivity.class));
    }

    public void onSearchViewTextChanged(String searchViewText) {
        autoCompleteDataRepository.setUserSearchTextQuery(searchViewText);
    }

    /**
     * Location Repository
     */

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
    }


}