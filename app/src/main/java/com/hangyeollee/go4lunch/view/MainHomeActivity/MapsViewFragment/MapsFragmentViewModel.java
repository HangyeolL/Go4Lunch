package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MapsFragmentViewModel extends ViewModel {

    private final MediatorLiveData<MapsFragmentViewState> mapsFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public MapsFragmentViewModel(LocationRepository locationRepository, NearbySearchDataRepository nearbySearchDataRepository, AutoCompleteDataRepository autoCompleteDataRepository) {

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<MyNearBySearchData> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData,
                location -> {
                    String locationToString = location.getLatitude() + "," + location.getLongitude();
                    return nearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
                }
        );

        LiveData<MyAutoCompleteData> myAutoCompleteDataLiveData = autoCompleteDataRepository.getAutoCompleteDataLiveData();

        mapsFragmentViewStateMediatorLiveData.addSource(locationLiveData, location ->
                combine(location, myNearBySearchDataLiveData.getValue(), myAutoCompleteDataLiveData.getValue())
        );

        mapsFragmentViewStateMediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData ->
                combine(locationLiveData.getValue(), myNearBySearchData, myAutoCompleteDataLiveData.getValue())
        );

        mapsFragmentViewStateMediatorLiveData.addSource(myAutoCompleteDataLiveData, myAutoCompleteData ->
                combine(locationLiveData.getValue(), myNearBySearchDataLiveData.getValue(), myAutoCompleteData)
        );


    }

    private void combine(@Nullable Location location, @Nullable MyNearBySearchData myNearBySearchData, @Nullable MyAutoCompleteData myAutoCompleteData) {
        if (location == null || myNearBySearchData == null) {
            return;
        }

        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        List<MapMarkerViewState> mapMarkerViewStateList = new ArrayList<>();

        // if myAutoCompleteData is null i want to display all the result of nearbySearch
        // if autoComplete is not null I want to do filtering
        if (myAutoCompleteData == null || myAutoCompleteData.getPredictions().isEmpty()) {
            for (Result result : myNearBySearchData.getResults()) {
                MapMarkerViewState mapMarkerViewState =
                        new MapMarkerViewState(
                                result.getPlaceId(),
                                new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()),
                                result.getName()
                        );

                mapMarkerViewStateList.add(mapMarkerViewState);
            }
        } else {
            for (Prediction prediction : myAutoCompleteData.getPredictions()) {
                for(Result result : myNearBySearchData.getResults()) {
                    if (prediction.getPlaceId().equals(result.getPlaceId()) &&
                            prediction.getStructuredFormatting().getMainText().contains(result.getName())) {

                        MapMarkerViewState mapMarkerViewState =
                                new MapMarkerViewState(
                                        result.getPlaceId(),
                                        new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()),
                                        result.getName()
                                );

                        mapMarkerViewStateList.add(mapMarkerViewState);
                    }
                }
            }
        }

        mapsFragmentViewStateMediatorLiveData.setValue(
                new MapsFragmentViewState(userLatLng, mapMarkerViewStateList)
        );
    }

    public LiveData<MapsFragmentViewState> getMapsFragmentViewStateLiveData() {
        return mapsFragmentViewStateMediatorLiveData;
    }

    // Etc.... //
    public BitmapDescriptor makeDrawableIntoBitmap(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
