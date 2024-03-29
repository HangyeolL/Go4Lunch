package com.hangyeollee.go4lunch.ui;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkManager;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hangyeollee.go4lunch.MainApplication;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.data.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.utils.DistanceCalculator;
import com.hangyeollee.go4lunch.utils.GoogleApiHolder;
import com.hangyeollee.go4lunch.ui.dispatcher.DispatcherViewModel;
import com.hangyeollee.go4lunch.ui.login.LogInViewModel;
import com.hangyeollee.go4lunch.ui.list.ListViewModel;
import com.hangyeollee.go4lunch.ui.main_home.MainHomeViewModel;
import com.hangyeollee.go4lunch.ui.map.MapViewModel;
import com.hangyeollee.go4lunch.ui.workmates.WorkmatesViewModel;
import com.hangyeollee.go4lunch.ui.place_detail.PlaceDetailViewModel;
import com.hangyeollee.go4lunch.ui.settings.SettingsViewModel;

import java.time.Clock;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application context;
    private final FirebaseAuth firebaseAuth;
    private final Clock clock;
    private final DistanceCalculator distanceCalculator;

    private final NearbySearchDataRepository nearbySearchDataRepository;
    private final PlaceDetailDataRepository placeDetailDataRepository;
    private final AutoCompleteDataRepository autoCompleteDataRepository;
    private final LocationRepository locationRepository;
    private final FirebaseRepository firebaseRepository;
    private final SettingRepository settingRepository;

    private static ViewModelFactory sViewModelFactory;

    /**
     * Singleton
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ViewModelFactory getInstance() {

        if (sViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                sViewModelFactory = new ViewModelFactory();
            }
        }
        return sViewModelFactory;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ViewModelFactory() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        GoogleApi googleApi = GoogleApiHolder.getGoogleApi();
        clock = Clock.systemDefaultZone();
        distanceCalculator = new DistanceCalculator();
        context = MainApplication.getInstance();

        locationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(context));
        firebaseRepository = new FirebaseRepository(firebaseAuth, firebaseFirestore);
        nearbySearchDataRepository = new NearbySearchDataRepository(googleApi);
        placeDetailDataRepository = new PlaceDetailDataRepository(googleApi);
        autoCompleteDataRepository = new AutoCompleteDataRepository(googleApi, locationRepository);
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
            return (T) new MainHomeViewModel(
                context,
                firebaseRepository,
                locationRepository,
                autoCompleteDataRepository,
                settingRepository,
                WorkManager.getInstance(context),
                clock
            );
        }
        else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(context, locationRepository, firebaseRepository, nearbySearchDataRepository, autoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(context, locationRepository, nearbySearchDataRepository, autoCompleteDataRepository, firebaseRepository, distanceCalculator);
        }
        else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(context, firebaseRepository, autoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(PlaceDetailViewModel.class)) {
            return (T) new PlaceDetailViewModel(context, placeDetailDataRepository, firebaseRepository);
        }
        else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(context, firebaseAuth, settingRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

}
