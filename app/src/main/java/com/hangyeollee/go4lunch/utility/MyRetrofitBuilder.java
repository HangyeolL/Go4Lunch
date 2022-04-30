package com.hangyeollee.go4lunch.utility;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitBuilder {

    public static final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/";

    public static Retrofit mRetrofit = null;

    public static Retrofit getRetrofit() {
        //to see the HTTP request in the logcat
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if(mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(GOOGLE_NEARBY_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return mRetrofit;
    }


}
