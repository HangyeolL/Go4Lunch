package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import android.location.Location;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapsFragmentViewState;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ListViewFragmentViewModel extends ViewModel {

    private LocationRepository mLocationRepository;
    private NearbySearchDataRepository mNearbySearchDataRepository;

    private MediatorLiveData<List<ListViewFragmentViewState>> listViewFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public ListViewFragmentViewModel(LocationRepository mLocationRepository, NearbySearchDataRepository mNearbySearchDataRepository) {
        this.mLocationRepository = mLocationRepository;
        this.mNearbySearchDataRepository = mNearbySearchDataRepository;

        LiveData<Location> locationLiveData = mLocationRepository.getLocationLiveData();

        LiveData<MyNearBySearchData> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData, new Function<Location, LiveData<MyNearBySearchData>>() {
            @Override
            public LiveData<MyNearBySearchData> apply(Location location) {
                String locationToString = location.getLatitude() + "," + location.getLongitude();
                return mNearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
            }
        });

        listViewFragmentViewStateMediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData -> {
            combine(myNearBySearchData);
        });
    }

    private void combine(@Nullable MyNearBySearchData myNearBySearchData) {
        if (myNearBySearchData == null) {
            return;
        }

        List<ListViewFragmentViewState> listViewFragmentViewStateList = new ArrayList<>();

        String name;
        String vicinity;
        boolean isOpen;
        double rating;
        String photoReference;
        String placeId;

        for (Result result : myNearBySearchData.getResults()) {
            name = result.getName();
            vicinity = result.getVicinity();
            isOpen = result.getOpeningHours().getOpenNow();
            rating = result.getRating();
            photoReference = result.getPhotos().get(0).getPhotoReference();
            placeId = result.getPlaceId();
        }

        if (mLocationRepository.getLocationLiveData().getValue() != null) {
            mLocationRepository.getLocationLiveData().getValue().getLatitude();
        }

    }

    public LiveData<List<ListViewFragmentViewState>> getListViewFragmentViewStateLiveData() {
        return listViewFragmentViewStateMediatorLiveData;
    }
}
