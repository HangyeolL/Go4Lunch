package com.hangyeollee.go4lunch.view.MainHomeActivity;

public class MainHomeActivityViewState {

    private final String providerId;
    private final String userName;
    private final String userEmail;
    private final String userPhotoUrl;
    private final String lunchRestaurantName;

    public MainHomeActivityViewState(String providerId, String userName, String userEmail, String userPhotoUrl, String lunchRestaurantName) {
        this.providerId = providerId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhotoUrl = userPhotoUrl;
        this.lunchRestaurantName = lunchRestaurantName;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public String getLunchRestaurantName() {
        return lunchRestaurantName;
    }
}
