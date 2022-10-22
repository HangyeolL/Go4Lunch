package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

public class PlaceDetailActivityRecyclerViewItemViewState {

    private final String userName;
    private final String userPhotoUrl;
    private final String goingToRestaurantText;

    public PlaceDetailActivityRecyclerViewItemViewState(String userName, String userPhotoUrl, String goingToRestaurantText) {
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.goingToRestaurantText = goingToRestaurantText;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public String getGoingToRestaurantText() {
        return goingToRestaurantText;
    }
}
