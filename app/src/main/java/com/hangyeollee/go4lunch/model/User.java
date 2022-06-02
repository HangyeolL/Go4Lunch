package com.hangyeollee.go4lunch.model;

public class User {

    private String name;
    private String photoUrl;

    public User() {
        // Empty constructor needed for Firestore
    }

    public User(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
