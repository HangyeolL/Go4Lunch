package com.hangyeollee.go4lunch.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearBySearchRepository;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private LocationRepository mLocationRepository;
    private NearBySearchRepository mNearBySearchRepository;

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
        mLocationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(context));
        mNearBySearchRepository = new NearBySearchRepository(MyRetrofitBuilder.getNearBySearchApi());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ListAndMapViewFragmentViewModel.class)) {
            return (T) new ListAndMapViewFragmentViewModel(mLocationRepository, mNearBySearchRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
