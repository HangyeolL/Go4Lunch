package com.hangyeollee.go4lunch.ui.main_home_activity.map_fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class MapMarkerViewState {
    private final String placeId;
    private final LatLng positionLatLng;
    private final String title;
    private final boolean isSelected;

    public MapMarkerViewState(String placeId, LatLng positionLatLng, String title, boolean isSelected) {
        this.placeId = placeId;
        this.positionLatLng = positionLatLng;
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getPlaceId() {
        return placeId;
    }

    public LatLng getPositionLatLng() {
        return positionLatLng;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String toString() {
        return "MapMarkerViewState{" +
                "placeId='" + placeId + '\'' +
                ", positionLatLng=" + positionLatLng +
                ", title='" + title + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapMarkerViewState that = (MapMarkerViewState) o;
        return isSelected == that.isSelected && Objects.equals(placeId, that.placeId) && Objects.equals(positionLatLng, that.positionLatLng) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, positionLatLng, title, isSelected);
    }
}
