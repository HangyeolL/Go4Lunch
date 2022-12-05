package com.hangyeollee.go4lunch.ui.map;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class MapMarkerViewState {
    private final String placeId;
    private final LatLng positionLatLng;
    private final String title;
    @DrawableRes
    private final int iconRes;
    @Nullable
    private final Integer tintRes;

    public MapMarkerViewState(String placeId, LatLng positionLatLng, String title, @DrawableRes int iconRes, @Nullable Integer tintRes) {
        this.placeId = placeId;
        this.positionLatLng = positionLatLng;
        this.title = title;
        this.iconRes = iconRes;
        this.tintRes = tintRes;
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

    @DrawableRes
    public int getIconRes() {
        return iconRes;
    }

    @Nullable
    public Integer getTintRes() {
        return tintRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapMarkerViewState that = (MapMarkerViewState) o;
        return iconRes == that.iconRes && Objects.equals(placeId, that.placeId) && Objects.equals(positionLatLng, that.positionLatLng) && Objects.equals(title, that.title) && Objects.equals(tintRes, that.tintRes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, positionLatLng, title, iconRes, tintRes);
    }

    @Override
    public String toString() {
        return "MapMarkerViewState{" +
            "placeId='" + placeId + '\'' +
            ", positionLatLng=" + positionLatLng +
            ", title='" + title + '\'' +
            ", iconRes=" + iconRes +
            ", tintRes=" + tintRes +
            '}';
    }
}
