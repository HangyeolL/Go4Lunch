package com.hangyeollee.go4lunch.data.model.neaerbyserach;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

public class OpeningHoursResponse {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    @TestOnly
    public OpeningHoursResponse(Boolean openNow) {
        this.openNow = openNow;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

}