package com.hangyeollee.go4lunch.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.hangyeollee.go4lunch.repository.FirebaseAuthRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private NearbySearchDataRepository mNearbySearchDataRepository;
    private PlaceDetailDataRepository mPlaceDetailDataRepository;
    private LocationRepository mLocationRepository;
    private FirebaseAuthRepository mFirebaseAuthRepository;

    private static ViewModelFactory mViewModelFactory;

    /**
     * Singleton
     */
    public static ViewModelFactory getInstance(Context context) {

        if (mViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                mViewModelFactory = new ViewModelFactory(context);
            }
        }
        return mViewModelFactory;
    }

    private ViewModelFactory(Context context) {
        mNearbySearchDataRepository = new NearbySearchDataRepository(MyRetrofitBuilder.getGoogleMapsApi());
        mPlaceDetailDataRepository = new PlaceDetailDataRepository(MyRetrofitBuilder.getGoogleMapsApi());
        mLocationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(context.getApplicationContext()));
        mFirebaseAuthRepository = new FirebaseAuthRepository();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(MapsAndListSharedViewModel.class)) {
            return (T) new MapsAndListSharedViewModel(mNearbySearchDataRepository, mLocationRepository);
        } else if (modelClass.isAssignableFrom(PlaceDetailActivityViewModel.class)) {
            return (T) new PlaceDetailActivityViewModel(mPlaceDetailDataRepository);
        } else if (modelClass.isAssignableFrom(LogInAndMainActivitySharedViewModel.class)) {
            return (T) new LogInAndMainActivitySharedViewModel(mFirebaseAuthRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
