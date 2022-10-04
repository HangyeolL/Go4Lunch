package com.hangyeollee.go4lunch.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hangyeollee.go4lunch.MainApplication;
import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;
import com.hangyeollee.go4lunch.view.LogInActivity.LogInActivityViewModel;
import com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment.ListViewFragmentViewModel;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MainHomeActivityViewModel;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsAndListSharedViewModel;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapsFragmentViewModel;
import com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment.WorkmatesFragmentViewModel;
import com.hangyeollee.go4lunch.view.PlaceDetailActivity.PlaceDetailActivityViewModel;
import com.hangyeollee.go4lunch.view.SettingsActivity.SettingsActivityViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;

    private NearbySearchDataRepository mNearbySearchDataRepository;
    private PlaceDetailDataRepository mPlaceDetailDataRepository;
    private AutoCompleteDataRepository mAutoCompleteDataRepository;
    private LocationRepository mLocationRepository;
    private FirebaseRepository mFirebaseRepository;

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
        mApplication = MainApplication.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        GoogleMapsApi googleMapsApi = MyRetrofitBuilder.getGoogleMapsApi();

        mNearbySearchDataRepository = new NearbySearchDataRepository(googleMapsApi);
        mPlaceDetailDataRepository = new PlaceDetailDataRepository(googleMapsApi);
        mAutoCompleteDataRepository = new AutoCompleteDataRepository(googleMapsApi);
        mLocationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(mApplication));
        mFirebaseRepository = new FirebaseRepository(firebaseAuth, firebaseFirestore);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(LogInActivityViewModel.class)) {
            return (T) new LogInActivityViewModel(mFirebaseRepository);
        }
        else if (modelClass.isAssignableFrom(MainHomeActivityViewModel.class)) {
            return (T) new MainHomeActivityViewModel(mFirebaseRepository, mLocationRepository);
        }
        else if (modelClass.isAssignableFrom(MapsFragmentViewModel.class)) {
            return (T) new MapsFragmentViewModel(mLocationRepository, mNearbySearchDataRepository, mAutoCompleteDataRepository);
        }
        else if (modelClass.isAssignableFrom(ListViewFragmentViewModel.class)) {
            return (T) new ListViewFragmentViewModel(mLocationRepository, mNearbySearchDataRepository);
        }
        else if (modelClass.isAssignableFrom(WorkmatesFragmentViewModel.class)) {
            return (T) new WorkmatesFragmentViewModel(mFirebaseRepository);
        }
        else if (modelClass.isAssignableFrom(PlaceDetailActivityViewModel.class)) {
            return (T) new PlaceDetailActivityViewModel(mPlaceDetailDataRepository, mFirebaseRepository);
        }
        else if (modelClass.isAssignableFrom(SettingsActivityViewModel.class)) {
            return (T) new SettingsActivityViewModel(mFirebaseRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

}