package com.hangyeollee.go4lunch.ui.list;

import androidx.annotation.ColorInt;

import java.util.Objects;

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
    private final int workmatesJoiningNumber;

    public ListItemViewState(String name, String vicinity, String isOpenText, int isOpenTextColor, float rating, String photoReference, String placeId, String distanceFromUserLocation, int workmatesJoiningNumber) {
        this.name = name;
        this.vicinity = vicinity;
        this.isOpenText = isOpenText;
        this.isOpenTextColor = isOpenTextColor;
        this.rating = rating;
        this.photoReference = photoReference;
        this.placeId = placeId;
        this.distanceFromUserLocation = distanceFromUserLocation;
        this.workmatesJoiningNumber = workmatesJoiningNumber;
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

    public int getWorkmatesJoiningNumber() {
        return workmatesJoiningNumber;
    }

    @Override
    public String toString() {
        return "ListItemViewState{" +
                "name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", isOpenText='" + isOpenText + '\'' +
                ", isOpenTextColor=" + isOpenTextColor +
                ", rating=" + rating +
                ", photoReference='" + photoReference + '\'' +
                ", placeId='" + placeId + '\'' +
                ", distanceFromUserLocation='" + distanceFromUserLocation + '\'' +
                ", workmatesJoiningNumber=" + workmatesJoiningNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItemViewState that = (ListItemViewState) o;
        return isOpenTextColor == that.isOpenTextColor && Float.compare(that.rating, rating) == 0 && workmatesJoiningNumber == that.workmatesJoiningNumber && Objects.equals(name, that.name) && Objects.equals(vicinity, that.vicinity) && Objects.equals(isOpenText, that.isOpenText) && Objects.equals(photoReference, that.photoReference) && Objects.equals(placeId, that.placeId) && Objects.equals(distanceFromUserLocation, that.distanceFromUserLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vicinity, isOpenText, isOpenTextColor, rating, photoReference, placeId, distanceFromUserLocation, workmatesJoiningNumber);
    }
}
