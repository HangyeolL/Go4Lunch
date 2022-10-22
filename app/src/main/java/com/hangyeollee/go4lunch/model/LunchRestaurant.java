package com.hangyeollee.go4lunch.model;

// TODO Hangye @Nullable !
public class LunchRestaurant {
    private String restaurantId;
    private String userId;
    private String restaurantMame;
    private String date;

    // TODO Hangye test without this empty constructor ?
    @SuppressWarnings("unused") // For Firestore deserialization
    public LunchRestaurant() { }

    public LunchRestaurant(String restaurantId, String userId, String restaurantMame, String date) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.restaurantMame = restaurantMame;
        this.date = date;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRestaurantMame() {
        return restaurantMame;
    }

    public String getDate() {
        return date;
    }
}
