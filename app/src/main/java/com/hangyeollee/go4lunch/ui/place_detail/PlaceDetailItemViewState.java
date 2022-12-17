package com.hangyeollee.go4lunch.ui.place_detail;

import java.util.Objects;

public class PlaceDetailItemViewState {

    private final String userName;
    private final String userPhotoUrl;

    public PlaceDetailItemViewState(String userName, String userPhotoUrl) {
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    @Override
    public String toString() {
        return "PlaceDetailItemViewState{" +
            "userName='" + userName + '\'' +
            ", userPhotoUrl='" + userPhotoUrl + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceDetailItemViewState that = (PlaceDetailItemViewState) o;
        return Objects.equals(userName, that.userName) && Objects.equals(userPhotoUrl, that.userPhotoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userPhotoUrl);
    }
}
