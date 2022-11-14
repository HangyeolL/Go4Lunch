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
import com.hangyeollee.go4lunch.utils.MyRetrofitBuilder;
import com.hangyeollee.go4lunch.ui.dispatcher_activity.DispatcherViewModel;
import com.hangyeollee.go4lunch.ui.logIn_activity.LogInViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment.ListViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.MainHomeViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.map_fragment.MapViewModel;
import com.hangyeollee.go4lunch.ui.main_home_activity.workmates_fragment.WorkmatesViewModel;
import com.hangyeollee.go4lunch.ui.place_detail_activity.PlaceDetailViewModel;
import com.hangyeollee.go4lunch.ui.settings_activity.SettingsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final FirebaseAuth firebaseAuth;

    private final NearbySearchDataRepository mNearbySearchDataRepository;
    private final PlaceDetailDataRepository mPlaceDetailDataRepository;
    private final AutoCompleteDataRepository mAutoCompleteDataRepository;
    private final LocationRepository mLocationRepository;
    private final FirebaseRepository mFirebaseRepository;

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

        mApplication = MainApplication.getInstance();
        mLocationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(mApplication));
        mFirebaseRepository = new FirebaseRepository(firebaseAuth, firebaseFirestore);
        mNearbySearchDataRepository = new NearbySearchDataRepository(googleMapsApi);
        mPlaceDetailDataRepository = new PlaceDetailDataRepository(googleMapsApi);
        mAutoCompleteDataRepository = new AutoCompleteDataRepository(googleMapsApi, mLocationRepository);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DispatcherViewModel.class)) {
            return (T) new DispatcherViewModel(firebaseAuth);
        }
        else if (modelClass.isAssignableFrom(LogInViewModel.class)) {
            return (T) new LogInViewModel(mFirebaseRepository);
        }
        else if (modelClass.isAssignableFrom(MainHomeViewModel.class)) {
            return (T) new MainHomeViewModel(mApplication, mFirebaseRepository, mLocationRepository, mAutoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(mApplication, mLocationRepository, mNearbySearchDataRepository, mAutoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(mLocationRepository, mNearbySearchDataRepository, mAutoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(mApplication, mFirebaseRepository, mAutoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(PlaceDetailViewModel.class)) {
            return (T) new PlaceDetailViewModel(mApplication, mPlaceDetailDataRepository, mFirebaseRepository);
        }
        else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(mApplication, mFirebaseRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

}
