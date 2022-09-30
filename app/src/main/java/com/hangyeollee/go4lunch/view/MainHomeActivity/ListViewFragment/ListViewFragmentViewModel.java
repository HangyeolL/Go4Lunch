package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import android.location.Location;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ListViewFragmentViewModel extends ViewModel {

    private LocationRepository mLocationRepository;
    private NearbySearchDataRepository mNearbySearchDataRepository;

    private MediatorLiveData<ListViewFragmentViewState> listViewFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

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

        List<ListViewFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        String name;
        String vicinity;
        Boolean isOpen = null;
        float rating = 0;
        String photoReference = "";
        String placeId;
        Location resultLocation = new Location("restaurant location");
        String distanceBetween = "";

        if (mLocationRepository.getLocationLiveData().getValue() != null) {
            distanceBetween = String.format("%.0f", mLocationRepository.getLocationLiveData().getValue().distanceTo(resultLocation)) + "m";
        }

        for (Result result : myNearBySearchData.getResults()) {
            name = result.getName();
            vicinity = result.getVicinity();
            if (result.getOpeningHours() != null) {
                isOpen = result.getOpeningHours().getOpenNow();
            }
            if (result.getRating() != null) {
                rating = result.getRating().floatValue();
            }
            if (result.getPhotos() != null) {
                photoReference = result.getPhotos().get(0).getPhotoReference();
            }

            placeId = result.getPlaceId();
            resultLocation.setLatitude(result.getGeometry().getLocation().getLat());
            resultLocation.setLongitude(result.getGeometry().getLocation().getLng());

            ListViewFragmentRecyclerViewItemViewState recyclerViewItemViewState = new ListViewFragmentRecyclerViewItemViewState(
                            name, vicinity, isOpen, rating, photoReference, placeId, distanceBetween
            );

            recyclerViewItemViewStateList.add(recyclerViewItemViewState);
        }

        listViewFragmentViewStateMediatorLiveData.setValue(new ListViewFragmentViewState(recyclerViewItemViewStateList, false));

    }

    public LiveData<ListViewFragmentViewState> getListViewFragmentViewStateLiveData() {
        return listViewFragmentViewStateMediatorLiveData;
    }
}
