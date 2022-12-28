package com.hangyeollee.go4lunch.data.model.neaerbyserach;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

public class GeometryResponse {

    @SerializedName("location")
    @Expose
    private LocationResponse location;
    @SerializedName("viewport")
    @Expose
    private ViewportResponse viewport;

    @TestOnly
    public GeometryResponse(LocationResponse location) {
        this.location = location;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse locationResponse) {
        this.location = locationResponse;
    }

    public ViewportResponse getViewport() {
        return viewport;
    }

    public void setViewport(ViewportResponse viewportResponse) {
        this.viewport = viewportResponse;
    }

}
