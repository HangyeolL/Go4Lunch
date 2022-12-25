package com.hangyeollee.go4lunch.data.model.neaerbyserach;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

public class GeometryResponse {

    @SerializedName("locationResponse")
    @Expose
    private LocationResponse locationResponse;
    @SerializedName("viewportResponse")
    @Expose
    private ViewportResponse viewportResponse;

    @TestOnly
    public GeometryResponse(LocationResponse locationResponse) {
        this.locationResponse = locationResponse;
    }

    public LocationResponse getLocation() {
        return locationResponse;
    }

    public void setLocation(LocationResponse locationResponse) {
        this.locationResponse = locationResponse;
    }

    public ViewportResponse getViewport() {
        return viewportResponse;
    }

    public void setViewport(ViewportResponse viewportResponse) {
        this.viewportResponse = viewportResponse;
    }

}
