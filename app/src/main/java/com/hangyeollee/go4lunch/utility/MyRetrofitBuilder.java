package com.hangyeollee.go4lunch.utility;

import com.hangyeollee.go4lunch.NearbySearchApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitBuilder {

    public static final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com";

    static Retrofit mRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(GOOGLE_NEARBY_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    static NearbySearchApi mNearbySearchApi = mRetrofitBuilder.create(NearbySearchApi.class);

    /**
     * Singleton
     */
    public static NearbySearchApi getRetrofitBuilder() {
        return mNearbySearchApi;
    }
}
