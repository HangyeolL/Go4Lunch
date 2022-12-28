package com.hangyeollee.go4lunch.data.model.placedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.OpeningHoursResponse;

import org.jetbrains.annotations.TestOnly;

import java.util.List;

public class ResultResponse {

    @SerializedName("geometry")
    @Expose
    private GeometryResponse geometry;
    @SerializedName("international_phone_number")
    @Expose
    private String internationalPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHoursResponse openingHours;
    @SerializedName("photos")
    @Expose
    private List<PhotoResponse> photos;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("website")
    @Expose
    private String website;

    @TestOnly
    public ResultResponse(GeometryResponse geometry, String internationalPhoneNumber, String name, OpeningHoursResponse openingHours, List<PhotoResponse> photos, Double rating, String vicinity, String website) {
        this.geometry = geometry;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
        this.rating = rating;
        this.vicinity = vicinity;
        this.website = website;
    }

    public GeometryResponse getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryResponse geometryResponse) {
        this.geometry = geometryResponse;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHoursResponse getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHoursResponse openingHoursResponse) {
        this.openingHours = openingHoursResponse;
    }

    public List<PhotoResponse> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoResponse> photos) {
        this.photos = photos;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}