package com.hangyeollee.go4lunch.ui.main_home;

public class MainHomeViewState {

    private final String providerId;
    private final String userName;
    private final String userEmail;
    private final String userPhotoUrl;
    private final String lunchRestaurantName;

    public MainHomeViewState(String providerId, String userName, String userEmail, String userPhotoUrl, String lunchRestaurantName) {
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
