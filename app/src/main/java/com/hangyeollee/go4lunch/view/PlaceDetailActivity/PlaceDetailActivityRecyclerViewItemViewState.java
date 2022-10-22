package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

public class PlaceDetailActivityRecyclerViewItemViewState {

    private final String userName;
    private final String userPhotoUrl;

    public PlaceDetailActivityRecyclerViewItemViewState(String userName, String userPhotoUrl) {
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
