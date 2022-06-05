package com.hangyeollee.go4lunch.model;

public class LikedRestaurant {
    private String id;
    private String name;

    public LikedRestaurant() { }

    public LikedRestaurant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
