package com.hangyeollee.go4lunch.ui.place_detail_activity;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.List;

public class PlaceDetailViewState {

    private final String photoUrl;
    private final String name;
    private final String address;
    private final float rating;
    private final String internationalPhoneNumber;
    private final String website;
    private final List<PlaceDetailItemViewState> recyclerViewItemViewStateList;
    @ColorRes
    private final int likeButtonColor;
    @ColorRes
    private final int floatActButtonColor;
    private final boolean isSelectedAsLikedRestaurant;
    private final boolean isSelectedAsLunchRestaurant;

    public PlaceDetailViewState(
        String photoUrl,
        String name,
        String address,
        float rating,
        String internationalPhoneNumber,
        String website,
        List<PlaceDetailItemViewState> recyclerViewItemViewStateList,
        @ColorRes
        int likeButtonColor,
        @ColorRes
        int floatActButtonColor,
        boolean isSelectedAsLikedRestaurant,
        boolean isSelectedAsLunchRestaurant
    ) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.website = website;
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
        this.likeButtonColor = likeButtonColor;
        this.floatActButtonColor = floatActButtonColor;
        this.isSelectedAsLikedRestaurant = isSelectedAsLikedRestaurant;
        this.isSelectedAsLunchRestaurant= isSelectedAsLunchRestaurant;

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

    public List<PlaceDetailItemViewState> getRecyclerViewItemViewStateList() {
        return recyclerViewItemViewStateList;
    }

    @ColorRes
    public int getLikeButtonColor() {
        return likeButtonColor;
    }

    @ColorRes
    public int getFloatActButtonColor() {
        return floatActButtonColor;
    }

    public boolean isSelectedAsLikedRestaurant() {
        return isSelectedAsLikedRestaurant;
    }

    public boolean isSelectedAsLunchRestaurant() {
        return isSelectedAsLunchRestaurant;
    }
}
