package com.hangyeollee.go4lunch.data.model.neaerbyserachpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    @TestOnly
    public OpeningHours(Boolean openNow) {
        this.openNow = openNow;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

}