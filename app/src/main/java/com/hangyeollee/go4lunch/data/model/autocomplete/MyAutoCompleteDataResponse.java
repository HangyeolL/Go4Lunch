package com.hangyeollee.go4lunch.data.model.autocomplete;

import androidx.annotation.VisibleForTesting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

import java.util.List;

public class MyAutoCompleteDataResponse {

    @SerializedName("predictions")
    @Expose
    private final List<Prediction> predictions;
    @SerializedName("status")
    @Expose
    private final String status;

    @VisibleForTesting
    public MyAutoCompleteDataResponse(List<Prediction> predictions, String status) {
        this.predictions = predictions;
        this.status = status;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }
}
