package com.hangyeollee.go4lunch.utility;

import com.hangyeollee.go4lunch.api.GoogleMapsApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitBuilder {

    public static final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/";

    public static Retrofit mRetrofit = null;

    public static Retrofit getRetrofit() {
        //to see the HTTP request in the logcat
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

        if(mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(GOOGLE_NEARBY_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return mRetrofit;
    }

    public static GoogleMapsApi getGoogleMapsApi() {
       return getRetrofit().create(GoogleMapsApi.class);
    }

}
