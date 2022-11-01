package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.List;

public class PlaceDetailActivityViewState {

    private final String photoUrl;
    private final String name;
    private final String address;
    private final float rating;
    private final String internationalPhoneNumber;
    private final String website;

    private final List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList;
    @ColorRes
    private final int isSelectedAsLikedRestaurant;
    private final boolean isSelectedAsLunchRestaurant;

    public PlaceDetailActivityViewState(
        String photoUrl,
        String name,
        String address,
        float rating,
        String internationalPhoneNumber,
        String website,
        List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList,
        @ColorRes
        int isSelectedAsLikedRestaurant,
        boolean isSelectedAsLunchRestaurant
    ) {
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

    @NonNull
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

    @ColorRes
    public int getLikedButtonColorTint() {
        return isSelectedAsLikedRestaurant;
    }

    public boolean getSelectedAsLunchRestaurant() {
        return isSelectedAsLunchRestaurant;
    }
}
