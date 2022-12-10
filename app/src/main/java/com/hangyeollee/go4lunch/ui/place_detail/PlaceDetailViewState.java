package com.hangyeollee.go4lunch.ui.place_detail;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class PlaceDetailViewState {

    private final String photoUrl;
    private final String name;
    private final String address;
    private final float rating;
    private final String internationalPhoneNumber;
    private final String website;
    private final List<PlaceDetailItemViewState> recyclerViewItemViewStateList;
    @ColorInt
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
        @ColorInt
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

    @ColorInt
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

    @Override
    public String toString() {
        return "PlaceDetailViewState{" +
            "photoUrl='" + photoUrl + '\'' +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", rating=" + rating +
            ", internationalPhoneNumber='" + internationalPhoneNumber + '\'' +
            ", website='" + website + '\'' +
            ", recyclerViewItemViewStateList=" + recyclerViewItemViewStateList +
            ", likeButtonColor=" + likeButtonColor +
            ", floatActButtonColor=" + floatActButtonColor +
            ", isSelectedAsLikedRestaurant=" + isSelectedAsLikedRestaurant +
            ", isSelectedAsLunchRestaurant=" + isSelectedAsLunchRestaurant +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceDetailViewState that = (PlaceDetailViewState) o;
        return Float.compare(that.rating, rating) == 0 && likeButtonColor == that.likeButtonColor && floatActButtonColor == that.floatActButtonColor && isSelectedAsLikedRestaurant == that.isSelectedAsLikedRestaurant && isSelectedAsLunchRestaurant == that.isSelectedAsLunchRestaurant && Objects.equals(photoUrl, that.photoUrl) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(internationalPhoneNumber, that.internationalPhoneNumber) && Objects.equals(website, that.website) && Objects.equals(recyclerViewItemViewStateList, that.recyclerViewItemViewStateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoUrl, name, address, rating, internationalPhoneNumber, website, recyclerViewItemViewStateList, likeButtonColor, floatActButtonColor, isSelectedAsLikedRestaurant, isSelectedAsLunchRestaurant);
    }
}
