package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class MapMarkerViewState {
    private final String placeId;
    private final LatLng positionLatLng;
    private final String title;

    public MapMarkerViewState(String placeId, LatLng positionLatLng, String title) {
        this.placeId = placeId;
        this.positionLatLng = positionLatLng;
        this.title = title;
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

    @Override
    public String toString() {
        return "MapMarkerViewState{" +
                ", positionLatLng=" + positionLatLng +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
