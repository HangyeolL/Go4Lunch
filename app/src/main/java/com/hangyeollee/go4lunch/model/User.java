package com.hangyeollee.go4lunch.model;

public class User {
    private String id;
    private String name;
    private String photoUrl;

    public User() {
        // Empty constructor needed for Firestore
    }

    public User(String id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

}
