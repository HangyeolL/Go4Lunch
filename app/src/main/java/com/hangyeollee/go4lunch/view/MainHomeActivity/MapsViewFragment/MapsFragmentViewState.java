package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapsFragmentViewState {

    private final LatLng userLatLng;
    private final List<MapMarkerViewState> mapMarkerViewStateList;

    public MapsFragmentViewState(LatLng userLatLng, List<MapMarkerViewState> mapMarkerViewStateList) {
        this.userLatLng = userLatLng;
        this.mapMarkerViewStateList = mapMarkerViewStateList;
    }

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public List<MapMarkerViewState> getMapMarkerViewStateList() {
        return mapMarkerViewStateList;
    }

}
