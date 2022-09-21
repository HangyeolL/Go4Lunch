package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;

import java.util.List;

public class MapsFragmentViewState {

    private final String userLocation;
    private final LatLng userLatLng;
    private final List<Result> myNearBySearchDataResultList;
    private final boolean isProgressBarVisible;

    public MapsFragmentViewState(String userLocation, LatLng userLatLng, List<Result> myNearBySearchDataResultList, boolean isProgressBarVisible) {
        this.userLocation = userLocation;
        this.userLatLng = userLatLng;
        this.myNearBySearchDataResultList = myNearBySearchDataResultList;
        this.isProgressBarVisible = isProgressBarVisible;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public List<Result> getMyNearBySearchDataResultList() {
        return myNearBySearchDataResultList;
    }

    public boolean isProgressBarVisible() {
        return isProgressBarVisible;
    }
}
