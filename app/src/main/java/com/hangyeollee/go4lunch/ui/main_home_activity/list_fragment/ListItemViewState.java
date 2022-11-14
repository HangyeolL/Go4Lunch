package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

public class ListItemViewState {

    private final String name;
    private final String vicinity;
    private final Boolean isOpen;
    private final float rating;
    private final String photoReference;
    private final String placeId;
    private final String distanceFromUserLocation;

    public ListItemViewState(String name, String vicinity, Boolean isOpen, float rating, String photoReference, String placeId, String distanceFromUserLocation) {
        this.name = name;
        this.vicinity = vicinity;
        this.isOpen = isOpen;
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

    public Boolean isOpen() {
        return isOpen;
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
