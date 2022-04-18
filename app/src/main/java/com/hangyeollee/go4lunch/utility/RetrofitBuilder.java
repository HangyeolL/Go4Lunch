package com.hangyeollee.go4lunch.utility;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public static final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters";

    private Retrofit mRetrofit;

    Retrofit mRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(GOOGLE_NEARBY_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
