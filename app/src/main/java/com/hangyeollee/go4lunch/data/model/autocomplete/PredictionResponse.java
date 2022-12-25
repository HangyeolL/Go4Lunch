package com.hangyeollee.go4lunch.data.model.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredictionResponse {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("matched_substrings")
    @Expose
    private List<MatchedSubstringResponse> matchedSubstrings = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("structured_formatting")
    @Expose
    private StructuredFormattingResponse structuredFormattingResponse;
    @SerializedName("termResponses")
    @Expose
    private List<TermResponse> termResponses = null;
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MatchedSubstringResponse> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<MatchedSubstringResponse> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StructuredFormattingResponse getStructuredFormatting() {
        return structuredFormattingResponse;
    }

    public void setStructuredFormatting(StructuredFormattingResponse structuredFormattingResponse) {
        this.structuredFormattingResponse = structuredFormattingResponse;
    }

    public List<TermResponse> getTerms() {
        return termResponses;
    }

    public void setTerms(List<TermResponse> termResponses) {
        this.termResponses = termResponses;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}