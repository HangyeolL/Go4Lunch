package com.hangyeollee.go4lunch.ui.main_home_activity.map_fragment;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapMarkerViewState that = (MapMarkerViewState) o;
        return Objects.equals(placeId, that.placeId) && Objects.equals(positionLatLng, that.positionLatLng) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, positionLatLng, title);
    }

}
