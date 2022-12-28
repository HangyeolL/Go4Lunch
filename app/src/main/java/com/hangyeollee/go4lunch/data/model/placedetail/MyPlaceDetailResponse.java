package com.hangyeollee.go4lunch.data.model.placedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

import java.util.List;

public class MyPlaceDetailResponse {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions;
    @SerializedName("result")
    @Expose
    private ResultResponse result;
    @SerializedName("status")
    @Expose
    private String status;

    public MyPlaceDetailResponse(List<Object> htmlAttributions, ResultResponse result, String status) {
        this.htmlAttributions = htmlAttributions;
        this.result = result;
        this.status = status;
    }

    @TestOnly

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public ResultResponse getResult() {
        return result;
    }

    public void setResult(ResultResponse result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}