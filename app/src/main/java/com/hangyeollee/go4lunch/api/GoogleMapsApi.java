package com.hangyeollee.go4lunch.api;

import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApi {
    @GET("nearbysearch/json?")
    Call<MyNearBySearchData> getNearbySearchData(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String key);

    @GET("details/json?")
    Call<MyPlaceDetailData> getPlaceDetails(@Query("fields") String fields, @Query("place_id") String placeId, @Query("key") String key);

    @GET("autocomplete/json?")
    Call<MyAutoCompleteData> getAutoCompleteData(@Query("input") String input, @Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String key);
}
