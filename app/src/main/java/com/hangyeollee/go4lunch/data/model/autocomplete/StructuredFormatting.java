package com.hangyeollee.go4lunch.data.model.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StructuredFormatting {
    @SerializedName("main_text")
    @Expose
    private String mainText;
    @SerializedName("main_text_matched_substrings")
    @Expose
    private List<MainTextMatchedSubstringResponse> mainTextMatchedSubstringResponses = null;
    @SerializedName("secondary_text")
    @Expose
    private String secondaryText;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public List<MainTextMatchedSubstringResponse> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstringResponses;
    }

    public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstringResponse> mainTextMatchedSubstringResponses) {
        this.mainTextMatchedSubstringResponses = mainTextMatchedSubstringResponses;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

}