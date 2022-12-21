package com.hangyeollee.go4lunch.api;

import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteDataResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.MyPlaceDetailData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApi {
    @GET("nearbysearch/json?")
    Call<MyNearBySearchData> getNearbySearchData(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String key);

    @GET("details/json?")
    Call<MyPlaceDetailData> getPlaceDetails(@Query("fields") String fields, @Query("place_id") String placeId, @Query("key") String key);

    @GET("autocomplete/json?")
    Call<MyAutoCompleteDataResponse> getAutoCompleteData(@Query("input") String input, @Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("language") String language, @Query("strictbounds") boolean strictbounds, @Query("key") String key);
}
