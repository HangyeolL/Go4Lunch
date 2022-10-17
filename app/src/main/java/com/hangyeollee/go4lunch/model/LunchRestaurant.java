package com.hangyeollee.go4lunch.model;

// TODO Hangye @Nullable !
public class LunchRestaurant {
    private String restaurantId;
    private String userId;
    private String name;
    private String date;

    // TODO Hangye test without this empty constructor ?
    @SuppressWarnings("unused") // For Firestore deserialization
    public LunchRestaurant() { }

    public LunchRestaurant(String restaurantId, String userId, String name, String date) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.name = name;
        this.date = date;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
