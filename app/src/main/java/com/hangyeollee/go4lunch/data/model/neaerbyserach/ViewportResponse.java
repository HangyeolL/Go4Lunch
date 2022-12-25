package com.hangyeollee.go4lunch.data.model.neaerbyserach;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewportResponse {
    @SerializedName("northeastResponse")
    @Expose
    private NortheastResponse northeastResponse;
    @SerializedName("southwestResponse")
    @Expose
    private SouthwestResponse southwestResponse;

    public NortheastResponse getNortheast() {
        return northeastResponse;
    }

    public void setNortheast(NortheastResponse northeastResponse) {
        this.northeastResponse = northeastResponse;
    }

    public SouthwestResponse getSouthwest() {
        return southwestResponse;
    }

    public void setSouthwest(SouthwestResponse southwestResponse) {
        this.southwestResponse = southwestResponse;
    }

}
