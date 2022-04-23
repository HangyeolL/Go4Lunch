package com.hangyeollee.go4lunch;

import com.hangyeollee.go4lunch.model.MyNearBySearchData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearbySearchApi {

    @GET("/maps/NearbySearchApi/place/nearbysearch/json?keyword=cruise")
    Call<MyNearBySearchData> getRestaurantList(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String key);

}
