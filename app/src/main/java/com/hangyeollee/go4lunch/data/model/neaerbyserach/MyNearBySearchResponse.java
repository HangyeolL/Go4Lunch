package com.hangyeollee.go4lunch.data.model.neaerbyserach;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

import java.util.List;

public class MyNearBySearchResponse {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions;
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<ResultResponse> results;
    @SerializedName("status")
    @Expose
    private String status;

    @TestOnly
    public MyNearBySearchResponse(List<Object> htmlAttributions, String nextPageToken, List<ResultResponse> results, String status) {
        this.htmlAttributions = htmlAttributions;
        this.nextPageToken = nextPageToken;
        this.results = results;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<ResultResponse> getResults() {
        return results;
    }

    public void setResults(List<ResultResponse> resultResponses) {
        this.results = resultResponses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
