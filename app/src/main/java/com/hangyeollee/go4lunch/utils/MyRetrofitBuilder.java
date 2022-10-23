package com.hangyeollee.go4lunch.utils;

import com.hangyeollee.go4lunch.api.GoogleMapsApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitBuilder {

    private static final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/";

    private static Retrofit INSTANCE = null;

    private static Retrofit getRetrofit() {
        //to see the HTTP request in the logcat
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

        if(INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(GOOGLE_NEARBY_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return INSTANCE;
    }

    public static GoogleMapsApi getGoogleMapsApi() {
       return getRetrofit().create(GoogleMapsApi.class);
    }

}
