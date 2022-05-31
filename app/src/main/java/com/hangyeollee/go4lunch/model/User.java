package com.hangyeollee.go4lunch.model;

public class User {
    private String uId;
    private String name;
    private String email;
    private String photoUrl;

    private String chosenRestaurantName;
    private String restaurantId;
    private String date;

    public User() {
        // Empty constructor needed for Firestore
    }

    public User(String uId, String name, String email, String photoUrl, String chosenRestaurantName, String restaurantId, String date) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.chosenRestaurantName = chosenRestaurantName;
        this.restaurantId = restaurantId;
        this.date = date;
    }

    public String getUid() {
        return uId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
