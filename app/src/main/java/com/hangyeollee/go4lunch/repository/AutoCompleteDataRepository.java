package com.hangyeollee.go4lunch.repository;

import android.location.Location;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteDataRepository {

    private final GoogleMapsApi mGoogleMapsApi;
    private final LiveData<MyAutoCompleteData> mAutoCompleteDataLiveData;
    private final MutableLiveData<String> userInputMutableLiveData = new MutableLiveData<>();

    public AutoCompleteDataRepository(GoogleMapsApi googleMapsApi, LocationRepository locationRepository) {
        mGoogleMapsApi = googleMapsApi;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        mAutoCompleteDataLiveData = Transformations.switchMap(
                locationLiveData,
                location -> Transformations.switchMap(
                        userInputMutableLiveData,
                        input -> {
                            if (location == null) {
                                return new MutableLiveData<>();
                            }
                            String locationToString = location.getLatitude() + "," + location.getLongitude();
                            return fetchAndGetAutoCompleteData(input, locationToString);
                        }
                )
        );

//        mAutoCompleteDataLiveData = Transformations.switchMap(
//                userInputMutableLiveData, input -> Transformations.switchMap(
//                        locationLiveData, location -> {
//                            String locationToString = location.getLatitude() + "," + location.getLongitude();
//                            return fetchAndGetAutoCompleteData(input, locationToString);
//                        }
//                )
//        );

    }

    public LiveData<MyAutoCompleteData> getAutoCompleteDataLiveData() {
        return mAutoCompleteDataLiveData;
    }

    public void setUserSearchTextQuery(String userSearchTextQuery) {
        userInputMutableLiveData.setValue(userSearchTextQuery);
    }

    public LiveData<MyAutoCompleteData> fetchAndGetAutoCompleteData(String userInput, String location) {
        MutableLiveData<MyAutoCompleteData> mutableLiveData = new MutableLiveData<>();

        Call<MyAutoCompleteData> call = mGoogleMapsApi.getAutoCompleteData(userInput, location, 1000, "restaurant", "en", true, BuildConfig.PLACES_API_KEY);
        call.enqueue(new Callback<MyAutoCompleteData>() {
            @Override
            public void onResponse(Call<MyAutoCompleteData> call, Response<MyAutoCompleteData> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyAutoCompleteData> call, Throwable t) {
                Log.w("AutoComplete", "failed", t);
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }
}
