package com.hangyeollee.go4lunch.data.model.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StructuredFormattingResponse {
    @SerializedName("main_text")
    @Expose
    private String mainText;
    @SerializedName("main_text_matched_substrings")
    @Expose
    private List<MainTextMatchedSubstringResponse> mainTextMatchedSubstringResponses;
    @SerializedName("secondary_text")
    @Expose
    private String secondaryText;

    public String getMainText() {
        return mainText;
    }

    public List<MainTextMatchedSubstringResponse> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstringResponses;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

}