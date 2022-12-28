package com.hangyeollee.go4lunch.data.model.autocomplete;

import androidx.annotation.VisibleForTesting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyAutoCompleteResponse {

    @SerializedName("predictions")
    @Expose
    private final List<PredictionResponse> predictions;
    @SerializedName("status")
    @Expose
    private final String status;

    @VisibleForTesting
    public MyAutoCompleteResponse(List<PredictionResponse> predictions, String status) {
        this.predictions = predictions;
        this.status = status;
    }

    public List<PredictionResponse> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }
}
