package com.hangyeollee.go4lunch.ui.main_home;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Clock;
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

    private Clock clock;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();

    private MainHomeViewModel viewModel;

    @Before
    public void setUp(){
        application = Mockito.mock(Application.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);
        settingRepository = Mockito.mock(SettingRepository.class);

        clock = Mockito.mock(Clock.class);
        WorkManager workManager = Mockito.mock(WorkManager.class);

        android.location.Location userLocation = Mockito.mock(android.location.Location.class);
        when(userLocation.getLatitude()).thenReturn(11.12);
        when(userLocation.getLongitude()).thenReturn(11.11);
        locationMutableLiveData.setValue(userLocation);

        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();

        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
        Uri uri = Mockito.mock(Uri.class);

        doReturn("userId").when(firebaseUser).getUid();
        doReturn("userName").when(firebaseUser).getDisplayName();
        doReturn("userEmail").when(firebaseUser).getEmail();
        doReturn(uri).when(firebaseUser).getPhotoUrl();
        doReturn("userPhotoUrl").when(uri).toString();

        viewModel = new MainHomeViewModel(
          application,
          firebaseRepository,
          locationRepository,
          autoCompleteDataRepository,
          settingRepository,
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





}