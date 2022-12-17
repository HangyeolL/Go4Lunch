package com.hangyeollee.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "WorkmatesItemViewState{" +
            "userPhotoUrl='" + userPhotoUrl + '\'' +
            ", userName='" + userName + '\'' +
            ", userLunchRestaurantName='" + userLunchRestaurantName + '\'' +
            ", userLunchRestaurantId='" + userLunchRestaurantId + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesItemViewState that = (WorkmatesItemViewState) o;
        return Objects.equals(userPhotoUrl, that.userPhotoUrl) && userName.equals(that.userName) && userLunchRestaurantName.equals(that.userLunchRestaurantName) && Objects.equals(userLunchRestaurantId, that.userLunchRestaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPhotoUrl, userName, userLunchRestaurantName, userLunchRestaurantId);
    }
}
