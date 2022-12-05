package com.hangyeollee.go4lunch.ui.main_home_activity.map_fragment;

import static com.hangyeollee.go4lunch.utils.UtilBox.makeDrawableIntoBitmap;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.ui.place_detail_activity.PlaceDetailActivity;
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

        LiveData<MyNearBySearchData> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData,
                location -> {
                    String locationToString = location.getLatitude() + "," + location.getLongitude();
                    return nearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
                }
        );

        LiveData<MyAutoCompleteData> myAutoCompleteDataLiveData = autoCompleteDataRepository.getAutoCompleteDataLiveData();

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
                        myAutoCompleteData)
        );

    }

    private void combine(
            @Nullable Location location,
            @Nullable List<LunchRestaurant> lunchRestaurantList,
            @Nullable MyNearBySearchData myNearBySearchData,
            @Nullable MyAutoCompleteData myAutoCompleteData
    ) {
        if (location == null || myNearBySearchData == null) {
            return;
        }

        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        List<MapMarkerViewState> mapMarkerViewStateList = new ArrayList<>();

        boolean isSelected = false;

        // if myAutoCompleteData is null i want to display all the result of nearbySearch
        // if autoComplete is not null I want to do filtering
        if (myAutoCompleteData == null || myAutoCompleteData.getPredictions().isEmpty()) {
            for (Result result : myNearBySearchData.getResults()) {

                for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                    if (result.getPlaceId().equalsIgnoreCase(lunchRestaurant.getRestaurantId())
                            && result.getName().equalsIgnoreCase(lunchRestaurant.getRestaurantName())) {
                        isSelected = true;
                        break;
                    }
                }

                MapMarkerViewState mapMarkerViewState =
                        new MapMarkerViewState(
                                result.getPlaceId(),
                                new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()),
                                result.getName(), isSelected);

                mapMarkerViewStateList.add(mapMarkerViewState);
            }
        } else {
            for (Prediction prediction : myAutoCompleteData.getPredictions()) {
                for (Result result : myNearBySearchData.getResults()) {
                    if (prediction.getPlaceId().equals(result.getPlaceId()) &&
                            prediction.getStructuredFormatting().getMainText().contains(result.getName())) {

                        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                            if (result.getPlaceId().equalsIgnoreCase(lunchRestaurant.getRestaurantId())
                                    && result.getName().equalsIgnoreCase(lunchRestaurant.getRestaurantName())) {
                                isSelected = true;
                                break;
                            }
                        }

                        MapMarkerViewState mapMarkerViewState =
                                new MapMarkerViewState(
                                        result.getPlaceId(),
                                        new LatLng(
                                                result.getGeometry().getLocation().getLat(),
                                                result.getGeometry().getLocation().getLng()
                                        ),
                                        result.getName(), isSelected);

                        mapMarkerViewStateList.add(mapMarkerViewState);
                    }
                }
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

    // TODO HOW TO TEST THOSE ?

    public void onMapReady(List<MapMarkerViewState> mapMarkerViewStateList, GoogleMap googleMap) {
        for (MapMarkerViewState mapMarkerViewState : mapMarkerViewStateList) {
            googleMap.addMarker(
                    new MarkerOptions()
                            .icon(getMapIcon(mapMarkerViewState))
                            .position(mapMarkerViewState.getPositionLatLng())
                            .title(mapMarkerViewState.getTitle())
            );
        }
    }

    public void onMarkerClicked(List<MapMarkerViewState> mapMarkerViewStateList, Marker marker) {
        for (MapMarkerViewState mapMarkerViewState : mapMarkerViewStateList) {
            if (marker.getTitle().equalsIgnoreCase(mapMarkerViewState.getTitle())) {
                intentSingleLiveEvent.setValue(PlaceDetailActivity.navigate(context, mapMarkerViewState.getPlaceId()));
                Log.d("Hangyeol", "markerName: " + marker.getTitle());
                break;
            }
        }

    }

    /**
     * ETC
     */

    private BitmapDescriptor getMapIcon(MapMarkerViewState mapMarkerViewState) {
        Drawable markerIconDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_twotone_dining_24, null);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable);
        if (mapMarkerViewState.isSelected()) {
            DrawableCompat.setTint(wrappedDrawable, context.getColor(R.color.green));
        }
        return makeDrawableIntoBitmap(wrappedDrawable);
    }




}
