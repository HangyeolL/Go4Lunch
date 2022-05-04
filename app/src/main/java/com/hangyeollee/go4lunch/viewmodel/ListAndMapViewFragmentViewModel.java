package com.hangyeollee.go4lunch.viewmodel;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearBySearchRepository;

public class ListAndMapViewFragmentViewModel extends ViewModel {

    private LocationRepository mLocationRepository;
    private NearBySearchRepository mNearBySearchRepository;

    public ListAndMapViewFragmentViewModel(LocationRepository locationRepository, NearBySearchRepository nearBySearchRepository) {
        mLocationRepository = locationRepository;
        mNearBySearchRepository = nearBySearchRepository;
    }

    public LiveData<Location> getLocationLiveData() {
        return mLocationRepository.getLocationLiveData();
    }

    public LiveData<MyNearBySearchData> getNearBySearchLiveData(String location) {
        return mNearBySearchRepository.getNearbySearchLiveData(location);
    }
}
