package com.hangyeollee.go4lunch.ui.place_detail_activity;

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
}
