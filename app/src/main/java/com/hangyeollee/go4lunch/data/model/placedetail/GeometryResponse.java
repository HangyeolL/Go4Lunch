package com.hangyeollee.go4lunch.data.model.placedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryResponse {

    @SerializedName("locationResponse")
    @Expose
    private LocationResponse locationResponse;

    public LocationResponse getLocation() {
        return locationResponse;
    }

    public void setLocation(LocationResponse locationResponse) {
        this.locationResponse = locationResponse;
    }

}
