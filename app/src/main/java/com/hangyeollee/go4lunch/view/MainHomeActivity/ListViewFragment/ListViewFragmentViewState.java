package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;

import java.util.List;

public class ListViewFragmentViewState {

    private final String name;
    private final String vicinity;
    private final boolean isOpen;
    private final double rating;
    private final String photoReference;
    private final String placeId;
    private final float distanceFromUserLocation;

    private final boolean isProgressBarVisible;

    public ListViewFragmentViewState(String name, String vicinity, boolean isOpen, double rating, String photoReference, String placeId, float distanceFromUserLocation, boolean isProgressBarVisible) {
        this.name = name;
        this.vicinity = vicinity;
        this.isOpen = isOpen;
        this.rating = rating;
        this.photoReference = photoReference;
        this.placeId = placeId;
        this.distanceFromUserLocation = distanceFromUserLocation;
        this.isProgressBarVisible = isProgressBarVisible;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public double getRating() {
        return rating;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public String getPlaceId() {
        return placeId;
    }

    public float getDistanceFromUserLocation() {
        return distanceFromUserLocation;
    }

    public boolean isProgressBarVisible() {
        return isProgressBarVisible;
    }
}
