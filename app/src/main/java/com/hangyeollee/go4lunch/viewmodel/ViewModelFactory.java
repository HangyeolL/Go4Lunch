package com.hangyeollee.go4lunch.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private NearbySearchDataRepository mNearbySearchDataRepository;
    private LocationRepository mLocationRepository;

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
        mLocationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(context.getApplicationContext()));
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(MapsAndListSharedViewModel.class)) {
            return (T) new MapsAndListSharedViewModel(mNearbySearchDataRepository, mLocationRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
