package com.hangyeollee.go4lunch.api;

import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearbySearchApi {
    @GET("nearbysearch/json?keyword=cruise")
    Call<MyNearBySearchData> getNearbySearchData(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String key);
}
