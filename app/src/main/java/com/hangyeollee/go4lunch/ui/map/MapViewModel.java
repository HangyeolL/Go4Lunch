package com.hangyeollee.go4lunch.ui.map;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteResponse;
import com.hangyeollee.go4lunch.data.model.autocomplete.PredictionResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.MyNearBySearchResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.ResultResponse;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.ui.place_detail.PlaceDetailActivity;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MapViewModel extends ViewModel {

    private final Application context;

    private final MediatorLiveData<MapViewState> mapsFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    private final SingleLiveEvent<Intent> intentSingleLiveEvent = new SingleLiveEvent<>();

    public MapViewModel(
        Application context,
        LocationRepository locationRepository,
        FirebaseRepository firebaseRepository,
        NearbySearchDataRepository nearbySearchDataRepository,
        AutoCompleteDataRepository autoCompleteDataRepository
    ) {
        this.context = context;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<MyNearBySearchResponse> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData,
            location -> {
                String locationToString = location.getLatitude() + "," + location.getLongitude();
                return nearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
            }
        );

        LiveData<MyAutoCompleteResponse> myAutoCompleteDataLiveData = autoCompleteDataRepository.getAutoCompleteDataLiveData();

        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = firebaseRepository.getLunchRestaurantListOfAllUsers();

        mapsFragmentViewStateMediatorLiveData.addSource(locationLiveData, location ->
            combine(location,
                lunchRestaurantListLiveData.getValue(),
                myNearBySearchDataLiveData.getValue(),
                myAutoCompleteDataLiveData.getValue()
            )
        );

        mapsFragmentViewStateMediatorLiveData.addSource(lunchRestaurantListLiveData, lunchRestaurantList ->
            combine(locationLiveData.getValue(),
                lunchRestaurantList,
                myNearBySearchDataLiveData.getValue(),
                myAutoCompleteDataLiveData.getValue()
            )
        );

        mapsFragmentViewStateMediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData ->
            combine(locationLiveData.getValue(),
                lunchRestaurantListLiveData.getValue(),
                myNearBySearchData,
                myAutoCompleteDataLiveData.getValue()
            )
        );

        mapsFragmentViewStateMediatorLiveData.addSource(myAutoCompleteDataLiveData, myAutoCompleteData ->
            combine(locationLiveData.getValue(),
                lunchRestaurantListLiveData.getValue(),
                myNearBySearchDataLiveData.getValue(),
                myAutoCompleteData
            )
        );

    }

    private void combine(
        @Nullable Location location,
        @Nullable List<LunchRestaurant> lunchRestaurantList,
        @Nullable MyNearBySearchResponse myNearBySearchData,
        @Nullable MyAutoCompleteResponse myAutoCompleteResponse
    ) {
        if (location == null || myNearBySearchData == null) {
            return;
        }

        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        List<MapMarkerViewState> mapMarkerViewStateList = new ArrayList<>();

        for (ResultResponse resultResponse : myNearBySearchData.getResults()) {
            boolean shouldAppear = false;

            if (myAutoCompleteResponse == null || myAutoCompleteResponse.getPredictions().isEmpty()) {
                shouldAppear = true;
            } else {
                for (PredictionResponse predictionResponse : myAutoCompleteResponse.getPredictions()) {
                    if (predictionResponse.getPlaceId().equals(resultResponse.getPlaceId()) &&
                        predictionResponse.getDescription().contains(resultResponse.getName())) {
                        shouldAppear = true;
                        break;
                    }
                }
            }

            if (shouldAppear) {
                boolean isSelected = false;
                for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                    if (resultResponse.getPlaceId().equalsIgnoreCase(lunchRestaurant.getRestaurantId())
                        && resultResponse.getName().equalsIgnoreCase(lunchRestaurant.getRestaurantName())) {
                        isSelected = true;
                        break;
                    }
                }

                MapMarkerViewState mapMarkerViewState = new MapMarkerViewState(
                    resultResponse.getPlaceId(),
                    new LatLng(resultResponse.getGeometry().getLocation().getLat(), resultResponse.getGeometry().getLocation().getLng()),
                    resultResponse.getName(),
                    R.drawable.ic_twotone_dining_24,
                    isSelected ? R.color.green : null
                );

                mapMarkerViewStateList.add(mapMarkerViewState);
            }
        }

        mapsFragmentViewStateMediatorLiveData.setValue(
            new MapViewState(userLatLng, mapMarkerViewStateList)
        );
    }

    /**
     * GETTERS
     */

    public LiveData<MapViewState> getMapsFragmentViewStateLiveData() {
        return mapsFragmentViewStateMediatorLiveData;
    }

    public SingleLiveEvent<Intent> getIntentSingleLiveEvent() {
        return intentSingleLiveEvent;
    }

    /**
     * EVENTS
     */

    public void onMarkerClicked(String placeId) {
        intentSingleLiveEvent.setValue(PlaceDetailActivity.navigate(context, placeId));
        Log.d("Hangyeol", "placeId: " + placeId);
    }
}
