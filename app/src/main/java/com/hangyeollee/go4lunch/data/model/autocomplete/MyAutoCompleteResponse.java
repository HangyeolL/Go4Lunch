package com.hangyeollee.go4lunch.data.model.autocomplete;

import androidx.annotation.VisibleForTesting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyAutoCompleteResponse {

    @SerializedName("predictionResponses")
    @Expose
    private final List<PredictionResponse> predictionResponses;
    @SerializedName("status")
    @Expose
    private final String status;

    @VisibleForTesting
    public MyAutoCompleteResponse(List<PredictionResponse> predictionResponses, String status) {
        this.predictionResponses = predictionResponses;
        this.status = status;
    }

    public List<PredictionResponse> getPredictions() {
        return predictionResponses;
    }

    public String getStatus() {
        return status;
    }
}
