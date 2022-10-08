package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.net.Uri;

import com.google.firebase.auth.UserInfo;

import java.util.List;

public class MainHomeActivityViewState {

    private final String providerId;
    private final String userName;
    private final String userEmail;
    private final Uri userPhotoUrl;
    private final boolean isUserLoggedIn;


    public MainHomeActivityViewState(String providerId, String userName, String userEmail, Uri userPhotoUrl, boolean isUserLoggedIn) {
        this.providerId = providerId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhotoUrl = userPhotoUrl;
        this.isUserLoggedIn = isUserLoggedIn;

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

    public Uri getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }


}
