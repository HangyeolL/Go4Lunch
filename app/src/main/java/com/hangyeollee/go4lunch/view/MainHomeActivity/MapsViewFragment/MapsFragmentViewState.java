package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "MapsFragmentViewState{" +
                "userLatLng=" + userLatLng +
                ", mapMarkerViewStateList=" + mapMarkerViewStateList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapsFragmentViewState that = (MapsFragmentViewState) o;
        return Objects.equals(userLatLng, that.userLatLng) && Objects.equals(mapMarkerViewStateList, that.mapMarkerViewStateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLatLng, mapMarkerViewStateList);
    }
}
