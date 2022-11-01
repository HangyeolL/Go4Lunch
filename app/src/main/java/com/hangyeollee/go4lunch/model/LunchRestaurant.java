package com.hangyeollee.go4lunch.model;

import javax.annotation.Nullable;

@Nullable
public class LunchRestaurant {
    private String restaurantId;
    private String userId;
    private String restaurantName;
    private String date;

    @SuppressWarnings("unused") // For Firestore deserialization
    public LunchRestaurant() { }

    public LunchRestaurant(String restaurantId, String userId, String restaurantName, String date) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.date = date;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getDate() {
        return date;
    }
}
