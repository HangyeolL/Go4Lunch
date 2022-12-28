package com.hangyeollee.go4lunch.data.model.placedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryResponse {

    @SerializedName("location")
    @Expose
    private LocationResponse location;

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

}
