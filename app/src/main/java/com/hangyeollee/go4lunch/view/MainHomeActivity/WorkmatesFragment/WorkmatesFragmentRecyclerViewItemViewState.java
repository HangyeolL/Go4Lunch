package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

public class WorkmatesFragmentRecyclerViewItemViewState {

    private final String userPhotoUrl;
    private final String userName;
    private final String userLunchRestaurantName;
    private final String getUserLunchRestaurantId;

    public WorkmatesFragmentRecyclerViewItemViewState(String userPhotoUrl, String userName, String userLunchRestaurantName, String getUserLunchRestaurantId) {
        this.userPhotoUrl = userPhotoUrl;
        this.userName = userName;
        this.userLunchRestaurantName = userLunchRestaurantName;
        this.getUserLunchRestaurantId = getUserLunchRestaurantId;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserLunchRestaurantName() {
        return userLunchRestaurantName;
    }

    public String getGetUserLunchRestaurantId() {
        return getUserLunchRestaurantId;
    }
}
