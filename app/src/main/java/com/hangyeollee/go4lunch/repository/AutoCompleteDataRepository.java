package com.hangyeollee.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteDataRepository {

    private GoogleMapsApi mGoogleMapsApi;
    private MutableLiveData<MyAutoCompleteData> mAutoCompleteDataMutableLiveData = new MutableLiveData<>();

    public AutoCompleteDataRepository(GoogleMapsApi googleMapsApi) {
        mGoogleMapsApi = googleMapsApi;
    }

    public LiveData<MyAutoCompleteData> getAutoCompleteLiveData() {
        return mAutoCompleteDataMutableLiveData;
    }

    public void fetchData(String input, String location) {
        Call<MyAutoCompleteData> call = mGoogleMapsApi.getAutoCompleteData(input, location, 1000, "restaurant", BuildConfig.MAPS_API_KEY);
        call.enqueue(new Callback<MyAutoCompleteData>() {
            @Override
            public void onResponse(Call<MyAutoCompleteData> call, Response<MyAutoCompleteData> response) {
                mAutoCompleteDataMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyAutoCompleteData> call, Throwable t) {
                Log.w("AutoComplete", "failed", t);
                mAutoCompleteDataMutableLiveData.postValue(null);
            }
        });

    }
}
