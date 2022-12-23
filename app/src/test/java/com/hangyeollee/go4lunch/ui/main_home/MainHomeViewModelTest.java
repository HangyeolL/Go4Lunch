package com.hangyeollee.go4lunch.ui.main_home;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.location.Location;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
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
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class MainHomeViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private FirebaseRepository firebaseRepository;
    private LocationRepository locationRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;
    private SettingRepository settingRepository;
    private WorkManager workManager;
    private Clock clock;

    private FirebaseUser firebaseUser;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNotificationEnabledBooleanLiveData = new MutableLiveData<>();

    private MainHomeViewModel viewModel;

    @Before
    public void setUp(){
        application = Mockito.mock(Application.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);
        settingRepository = Mockito.mock(SettingRepository.class);
        workManager = Mockito.mock(WorkManager.class);
        clock = Clock.fixed(
            Instant.ofEpochSecond(1671632869), // 21/12/2022 - 15:27:49 - Paris
            ZoneOffset.UTC
        );

        android.location.Location userLocation = Mockito.mock(android.location.Location.class);
        when(userLocation.getLatitude()).thenReturn(11.12);
        when(userLocation.getLongitude()).thenReturn(11.11);
        locationMutableLiveData.setValue(userLocation);

        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());
        isNotificationEnabledBooleanLiveData.setValue(false);

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();
        doReturn(isNotificationEnabledBooleanLiveData).when(settingRepository).getIsNotificationEnabledLiveData();

        firebaseUser = Mockito.mock(FirebaseUser.class);
        Uri uri = Mockito.mock(Uri.class);

        doReturn(firebaseUser).when(firebaseRepository).getCurrentUser();
        doReturn("userId1").when(firebaseUser).getUid();
        doReturn("userName").when(firebaseUser).getDisplayName();
        doReturn("userEmail").when(firebaseUser).getEmail();
        doReturn(uri).when(firebaseUser).getPhotoUrl();
        doReturn("userPhotoUrl").when(uri).toString();

        doReturn("Email unavailable").when(application).getString(R.string.email_unavailable);
        doReturn("You didn\\'t decide where to lunch yet").when(application).getString(R.string.did_not_decide_where_to_lunch);

        viewModel = new MainHomeViewModel(
          application,
          firebaseRepository,
          locationRepository,
          autoCompleteDataRepository,
          settingRepository,
          workManager,
          clock
        );
    }

    @Test
    public void nominal_case () {
        //WHEN
        MainHomeViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getMainHomeActivityViewStateLiveData());

        //THEN
        MainHomeViewState expectedViewState = new MainHomeViewState(
            "userName", "userEmail", "userPhotoUrl", null
        );
        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void email_unavailable() {
        //GIVEN
        doReturn(null).when(firebaseUser).getEmail();

        //WHEN
        MainHomeViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getMainHomeActivityViewStateLiveData());

        //THEN
        MainHomeViewState expectedViewState = new MainHomeViewState(
            "userName", "Email unavailable", "userPhotoUrl", null
        );
        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void user_has_chosen_happy_food1_as_lunch_restaurant() {
        //GIVEN
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant(
                "placeId1",
                "userId1",
                "happy food1",
                "2022-12-01"
            )
        );
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        //WHEN
        MainHomeViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getMainHomeActivityViewStateLiveData());

        //THEN
        MainHomeViewState expectedViewState = new MainHomeViewState(
            "userName", "userEmail", "userPhotoUrl", "happy food1"
        );
        assertEquals(expectedViewState, viewState);
    }

    @Ignore //TODO NINO check
    @Test
    public void onUserLoggedIn_notification_is_enabled() {
        //GIVEN
        PeriodicWorkRequest workRequest = Mockito.mock(PeriodicWorkRequest.class);
        isNotificationEnabledBooleanLiveData.setValue(true);

        //WHEN
        viewModel.onUserLoggedIn();

        //THEN
        Mockito.verify(workManager).enqueueUniquePeriodicWork(
            "REMINDER_REQUEST",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        );
        Mockito.verify(firebaseRepository).saveUserInFirestore();
    }

    @Test
    public void onUserLoggedIn_notification_is_disabled() {
        //WHEN
        viewModel.onUserLoggedIn();

        //THEN
        Mockito.verify(workManager).cancelAllWork();
        Mockito.verify(firebaseRepository).saveUserInFirestore();
    }

    @Test
    public void onYourLunchClicked_user_did_not_choose_lunch_restaurant_yet() {
        //WHEN
        MainHomeViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getMainHomeActivityViewStateLiveData());
        viewModel.onYourLunchClicked(viewState);

        String actualString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());

        //THEN
        assertEquals("You didn\\'t decide where to lunch yet", actualString);
    }

    @Test
    public void onYourLunchClicked_user_chose_happy_food1_as_lunch_restaurant() {
        //GIVEN
        //GIVEN
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant(
                "placeId1",
                "userId1",
                "happy food1",
                "2022-12-01"
            )
        );
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        //WHEN
        MainHomeViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getMainHomeActivityViewStateLiveData());
        viewModel.onYourLunchClicked(viewState);

        String actualString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());

        //THEN
        assertEquals("happy food1", actualString);
    }

    @Test
    public void onLogOutClicked() {
        //WHEN
        viewModel.onLogOutClicked();

        //THEN
        Mockito.verify(firebaseRepository).signOutFromFirebaseAuth();
    }

    @Test
    public void searchView_input_test() {
        //GIVEN
        viewModel.onSearchViewTextChanged("input");

        //THEN
        Mockito.verify(autoCompleteDataRepository).setUserSearchTextQuery("input");
    }

    @Test
    public void startLocationRequest() {
        //GIVEN
        viewModel.startLocationRequest();

        //THEN
        Mockito.verify(locationRepository).startLocationRequest();
    }

    @Test
    public void stopLocationRequest() {
        //GIVEN
        viewModel.stopLocationRequest();

        //THEN
        Mockito.verify(locationRepository).stopLocationRequest();
    }

}