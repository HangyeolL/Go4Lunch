package com.hangyeollee.go4lunch.ui.main_home;

import java.util.Objects;

public class MainHomeViewState {

    private final String userName;
    private final String userEmail;
    private final String userPhotoUrl;
    private final String lunchRestaurantName;

    public MainHomeViewState(String userName, String userEmail, String userPhotoUrl, String lunchRestaurantName) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhotoUrl = userPhotoUrl;
        this.lunchRestaurantName = lunchRestaurantName;
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

    @Override
    public String toString() {
        return "MainHomeViewState{" +
            "userName='" + userName + '\'' +
            ", userEmail='" + userEmail + '\'' +
            ", userPhotoUrl='" + userPhotoUrl + '\'' +
            ", lunchRestaurantName='" + lunchRestaurantName + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainHomeViewState that = (MainHomeViewState) o;
        return Objects.equals(userName, that.userName) && Objects.equals(userEmail, that.userEmail) && Objects.equals(userPhotoUrl, that.userPhotoUrl) && Objects.equals(lunchRestaurantName, that.lunchRestaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userEmail, userPhotoUrl, lunchRestaurantName);
    }
}
