package com.hangyeollee.go4lunch.data.model.placedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.OpeningHoursResponse;

import org.jetbrains.annotations.TestOnly;

import java.util.List;

public class ResultResponse {

    @SerializedName("geometryResponse")
    @Expose
    private GeometryResponse geometryResponse;
    @SerializedName("international_phone_number")
    @Expose
    private String internationalPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHoursResponse openingHoursResponse;
    @SerializedName("photoResponses")
    @Expose
    private List<PhotoResponse> photoResponses = null;
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
    public ResultResponse(GeometryResponse geometryResponse, String internationalPhoneNumber, String name, OpeningHoursResponse openingHoursResponse, List<PhotoResponse> photoResponses, Double rating, String vicinity, String website) {
        this.geometryResponse = geometryResponse;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.name = name;
        this.openingHoursResponse = openingHoursResponse;
        this.photoResponses = photoResponses;
        this.rating = rating;
        this.vicinity = vicinity;
        this.website = website;
    }

    public GeometryResponse getGeometry() {
        return geometryResponse;
    }

    public void setGeometry(GeometryResponse geometryResponse) {
        this.geometryResponse = geometryResponse;
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
        return openingHoursResponse;
    }

    public void setOpeningHours(OpeningHoursResponse openingHoursResponse) {
        this.openingHoursResponse = openingHoursResponse;
    }

    public List<PhotoResponse> getPhotos() {
        return photoResponses;
    }

    public void setPhotos(List<PhotoResponse> photoResponses) {
        this.photoResponses = photoResponses;
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