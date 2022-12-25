package com.hangyeollee.go4lunch.data.model.neaerbyserach;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;

public class ResultResponse {
    @SerializedName("business_status")
    @Expose
    private String businessStatus;
    @SerializedName("geometryResponse")
    @Expose
    private GeometryResponse geometryResponse;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_background_color")
    @Expose
    private String iconBackgroundColor;
    @SerializedName("icon_mask_base_uri")
    @Expose
    private String iconMaskBaseUri;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHoursResponse openingHoursResponse;
    @SerializedName("photoResponses")
    @Expose
    private List<PhotoResponse> photoResponses;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("plus_code")
    @Expose
    private PlusCodeResponse plusCodeResponse;
    @SerializedName("price_level")
    @Expose
    private Integer priceLevel;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("types")
    @Expose
    private List<String> types;
    @SerializedName("user_ratings_total")
    @Expose
    private Integer userRatingsTotal;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("permanently_closed")
    @Expose
    private Boolean permanentlyClosed;

    @TestOnly
    public ResultResponse(GeometryResponse geometryResponse, String name, OpeningHoursResponse openingHoursResponse, List<PhotoResponse> photoResponses, String placeId, Double rating, String vicinity) {
        this.geometryResponse = geometryResponse;
        this.name = name;
        this.openingHoursResponse = openingHoursResponse;
        this.photoResponses = photoResponses;
        this.placeId = placeId;
        this.rating = rating;
        this.vicinity = vicinity;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public GeometryResponse getGeometry() {
        return geometryResponse;
    }

    public void setGeometry(GeometryResponse geometryResponse) {
        this.geometryResponse = geometryResponse;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(String iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public String getIconMaskBaseUri() {
        return iconMaskBaseUri;
    }

    public void setIconMaskBaseUri(String iconMaskBaseUri) {
        this.iconMaskBaseUri = iconMaskBaseUri;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public PlusCodeResponse getPlusCode() {
        return plusCodeResponse;
    }

    public void setPlusCode(PlusCodeResponse plusCodeResponse) {
        this.plusCodeResponse = plusCodeResponse;
    }

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Boolean getPermanentlyClosed() {
        return permanentlyClosed;
    }

    public void setPermanentlyClosed(Boolean permanentlyClosed) {
        this.permanentlyClosed = permanentlyClosed;
    }

}
