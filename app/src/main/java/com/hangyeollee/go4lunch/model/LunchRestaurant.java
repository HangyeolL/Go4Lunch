package com.hangyeollee.go4lunch.model;

public class LunchRestaurant {
    private String id;
    private String name;
    private String date;

    public LunchRestaurant() { }

    public LunchRestaurant(String id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
