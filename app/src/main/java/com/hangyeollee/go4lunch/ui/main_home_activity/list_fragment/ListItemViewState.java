package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import androidx.annotation.ColorInt;

public class ListItemViewState {

    private final String name;
    private final String vicinity;
    private final String isOpenText;
    @ColorInt
    private final int isOpenTextColor;
    private final float rating;
    private final String photoReference;
    private final String placeId;
    private final String distanceFromUserLocation;

    public ListItemViewState(String name, String vicinity, String isOpenText, int isOpenTextColor, float rating, String photoReference, String placeId, String distanceFromUserLocation) {
        this.name = name;
        this.vicinity = vicinity;
        this.isOpenText = isOpenText;
        this.isOpenTextColor = isOpenTextColor;
        this.rating = rating;
        this.photoReference = photoReference;
        this.placeId = placeId;
        this.distanceFromUserLocation = distanceFromUserLocation;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getIsOpenText() {
        return isOpenText;
    }

    public int getIsOpenTextColor() {
        return isOpenTextColor;
    }

    public float getRating() {
        return rating;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getDistanceFromUserLocation() {
        return distanceFromUserLocation;
    }
}
