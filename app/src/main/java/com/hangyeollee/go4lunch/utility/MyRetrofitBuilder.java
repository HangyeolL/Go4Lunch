package com.hangyeollee.go4lunch.utility;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitBuilder {

    public static final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com";

    public static Retrofit mRetrofit = null;

    public static Retrofit getRetrofit() {
        if(mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(GOOGLE_NEARBY_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
//
//    /**
//     * Singleton
//     */
//    public static NearbySearchApi getNearbySearchApi() {
//        return mRetrofit.create(NearbySearchApi.class);
//    }
}
