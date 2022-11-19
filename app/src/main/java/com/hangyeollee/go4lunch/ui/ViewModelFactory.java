package com.hangyeollee.go4lunch.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hangyeollee.go4lunch.MainApplication;
import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.data.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.utils.MyRetrofitBuilder;
import com.hangyeollee.go4lunch.ui.dispatcher_activity.DispatcherViewModel;
import com.hangyeollee.go4lunch.ui.logIn_activity.LogInViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment.ListViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.MainHomeViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.map_fragment.MapViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.workmates_fragment.WorkmatesViewModel;
import com.hangyeollee.go4lunch.ui.place_detail_activity.PlaceDetailViewModel;
import com.hangyeollee.go4lunch.ui.settings_activity.SettingsViewModel;

import java.time.Clock;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application context;
    private final FirebaseAuth firebaseAuth;
    private final Clock clock;

    private final NearbySearchDataRepository nearbySearchDataRepository;
    private final PlaceDetailDataRepository placeDetailDataRepository;
    private final AutoCompleteDataRepository autoCompleteDataRepository;
    private final LocationRepository locationRepository;
    private final FirebaseRepository firebaseRepository;
    private final SettingRepository settingRepository;

    private static ViewModelFactory mViewModelFactory;

    /**
     * Singleton
     */
    public static ViewModelFactory getInstance() {

        if (mViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                mViewModelFactory = new ViewModelFactory();
            }
        }
        return mViewModelFactory;
    }

    private ViewModelFactory() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        GoogleMapsApi googleMapsApi = MyRetrofitBuilder.getGoogleMapsApi();
        clock = Clock.systemDefaultZone();

        context = MainApplication.getInstance();
        locationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(context));
        firebaseRepository = new FirebaseRepository(firebaseAuth, firebaseFirestore);
        nearbySearchDataRepository = new NearbySearchDataRepository(googleMapsApi);
        placeDetailDataRepository = new PlaceDetailDataRepository(googleMapsApi);
        autoCompleteDataRepository = new AutoCompleteDataRepository(googleMapsApi, locationRepository);
        settingRepository = new SettingRepository(context);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DispatcherViewModel.class)) {
            return (T) new DispatcherViewModel(firebaseAuth);
        }
        else if (modelClass.isAssignableFrom(LogInViewModel.class)) {
            return (T) new LogInViewModel(context, firebaseAuth);
        }
        else if (modelClass.isAssignableFrom(MainHomeViewModel.class)) {
            return (T) new MainHomeViewModel(context, firebaseRepository, locationRepository, autoCompleteDataRepository, settingRepository);
        }
        else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(context, locationRepository, nearbySearchDataRepository, autoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(context, locationRepository, nearbySearchDataRepository, autoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(context, firebaseRepository, autoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(PlaceDetailViewModel.class)) {
            return (T) new PlaceDetailViewModel(context, placeDetailDataRepository, firebaseRepository);
        }
        else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(context, firebaseAuth, settingRepository, clock);
        }

        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

}
