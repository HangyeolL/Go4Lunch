package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import java.util.List;

public class PlaceDetailActivityViewState {

    private final String photoUrl;
    private final String name;
    private final String address;
    private final float rating;
    private final String internationalPhoneNumber;
    private final String website;

    private final List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList;
    private final boolean isSelectedAsLikedRestaurant;
    private final boolean isSelectedAsLunchRestaurant;

    public PlaceDetailActivityViewState(String photoUrl, String name, String address, float rating, String internationalPhoneNumber, String website, List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList, boolean isSelectedAsLikedRestaurant, boolean isSelectedAsLunchRestaurant) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.website = website;
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
        this.isSelectedAsLikedRestaurant = isSelectedAsLikedRestaurant;
        this.isSelectedAsLunchRestaurant = isSelectedAsLunchRestaurant;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public List<PlaceDetailActivityRecyclerViewItemViewState> getRecyclerViewItemViewStateList() {
        return recyclerViewItemViewStateList;
    }

    public boolean getSelectedAsLikedRestaurant() {
        return isSelectedAsLikedRestaurant;
    }

    public boolean getSelectedAsLunchRestaurant() {
        return isSelectedAsLunchRestaurant;
    }
}
