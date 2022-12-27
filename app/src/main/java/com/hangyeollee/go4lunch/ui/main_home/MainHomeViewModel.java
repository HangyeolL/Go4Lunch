package com.hangyeollee.go4lunch.ui.main_home;

import static com.hangyeollee.go4lunch.utils.UtilBox.resourceToUri;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.ui.login.LogInActivity;
import com.hangyeollee.go4lunch.ui.settings.SettingsActivity;
import com.hangyeollee.go4lunch.utils.NotificationWorker;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainHomeViewModel extends ViewModel {

    private final Application context;
    private final FirebaseRepository firebaseRepository;
    private final LocationRepository locationRepository;
    private final AutoCompleteDataRepository autoCompleteDataRepository;
    private final SettingRepository settingRepository;
    private final WorkManager workManager;
    private final Clock clock;

    private static final String REMINDER_REQUEST = "REMINDER_REQUEST";

    private final MediatorLiveData<MainHomeViewState> mainHomeActivityViewStateMediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Intent> intentSingleLiveEvent = new SingleLiveEvent<>();

    public MainHomeViewModel(
            Application context,
            FirebaseRepository firebaseRepository,
            LocationRepository locationRepository,
            AutoCompleteDataRepository autoCompleteDataRepository,
            SettingRepository settingRepository,
            WorkManager workManager,
            Clock clock
    ) {
        this.context = context;
        this.firebaseRepository = firebaseRepository;
        this.locationRepository = locationRepository;
        this.autoCompleteDataRepository = autoCompleteDataRepository;
        this.settingRepository = settingRepository;
        this.workManager = workManager;
        this.clock = clock;


        LiveData<Location> locationLiveData = this.locationRepository.getLocationLiveData();
        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = this.firebaseRepository.getLunchRestaurantListOfAllUsers();

        mainHomeActivityViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, lunchRestaurantListLiveData.getValue())
        );
    }

    private void combine(Location location, List<LunchRestaurant> lunchRestaurantList) {
        if (location == null) {
            return;
        }

        final String userName;
        final String userEmail;
        final String userPhotoUrl;
        String lunchRestaurantName = null;

        FirebaseUser firebaseUser = firebaseRepository.getCurrentUser();

        userName = firebaseUser.getDisplayName();

        if (firebaseUser.getEmail() == null || firebaseUser.getEmail().equals("")) {
            userEmail = context.getString(R.string.email_unavailable);
        } else {
            userEmail = firebaseUser.getEmail();
        }

        if (firebaseUser.getPhotoUrl() == null || firebaseUser.getPhotoUrl().toString().isEmpty()) {
            userPhotoUrl = resourceToUri(context, R.drawable.ic_baseline_person_outline_24);
        } else {
            userPhotoUrl = firebaseUser.getPhotoUrl().toString();
        }

        if (lunchRestaurantList != null) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (firebaseUser.getUid().equals(lunchRestaurant.getUserId())) {
                    lunchRestaurantName = lunchRestaurant.getRestaurantName();
                }
            }
        }

        MainHomeViewState mainHomeViewState = new MainHomeViewState(
               userName, userEmail, userPhotoUrl, lunchRestaurantName);

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
    @NonNull
    public void onUserLoggedIn() {
        firebaseRepository.saveUserInFirestore();

        if (settingRepository.getIsNotificationEnabledLiveData().getValue()) {
            LocalDateTime currentDate = LocalDateTime.now(clock);
            LocalDateTime thisNoon = currentDate.with(LocalTime.of(12, 0));

            if (currentDate.isAfter(thisNoon)) {
                thisNoon = thisNoon.plusDays(1);
            }

            long timeLeft = ChronoUnit
                    .SECONDS
                    .between(currentDate, thisNoon);

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                    .Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                    .setInitialDelay(timeLeft, TimeUnit.MILLISECONDS)
                    .build();

            workManager.enqueueUniquePeriodicWork(
                    REMINDER_REQUEST,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
            );

        } else {
            workManager.cancelAllWork();
        }
    }

    public void onYourLunchClicked(MainHomeViewState mainHomeViewState) {
        if (mainHomeViewState.getLunchRestaurantName() == null) {
            toastMessageSingleLiveEvent.setValue(context.getString(R.string.did_not_decide_where_to_lunch));
        } else {
            toastMessageSingleLiveEvent.setValue(mainHomeViewState.getLunchRestaurantName());
        }
    }

    public void onSettingsClicked() {
        intentSingleLiveEvent.setValue(SettingsActivity.navigate(context));
    }

    public void onLogOutClicked() {
        firebaseRepository.signOutFromFirebaseAuth();
        intentSingleLiveEvent.setValue(LogInActivity.navigate(context));
    }

    public void onSearchViewTextChanged(String searchViewText) {
        autoCompleteDataRepository.setUserSearchTextQuery(searchViewText);
    }

    /**
     * LocationResponse Repository
     */

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        locationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        locationRepository.stopLocationRequest();
    }

}