package com.hangyeollee.go4lunch.ui.main_home_activity.workmates_fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WorkmatesItemViewState {

    @Nullable
    private final String userPhotoUrl;

    @NonNull
    private final String userName;

    @NonNull
    private final String userLunchRestaurantName;

    @Nullable
    private final String userLunchRestaurantId;

    public WorkmatesItemViewState(
        @Nullable String userPhotoUrl,
        @NonNull String userName,
        @NonNull String userLunchRestaurantName,
        @Nullable String userLunchRestaurantId
    ) {
        this.userPhotoUrl = userPhotoUrl;
        this.userName = userName;
        this.userLunchRestaurantName = userLunchRestaurantName;
        this.userLunchRestaurantId = userLunchRestaurantId;
    }

    @Nullable
    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @NonNull
    public String getUserLunchRestaurantName() {
        return userLunchRestaurantName;
    }

    @Nullable
    public String getUserLunchRestaurantId() {
        return userLunchRestaurantId;
    }
}
