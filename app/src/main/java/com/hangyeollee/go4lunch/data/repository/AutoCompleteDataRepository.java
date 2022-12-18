package com.hangyeollee.go4lunch.data.repository;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteDataRepository {

    private final GoogleApi mGoogleApi;
    private final LiveData<MyAutoCompleteData> mAutoCompleteDataLiveData;
    private final MutableLiveData<String> userInputMutableLiveData = new MutableLiveData<>();

    public AutoCompleteDataRepository(GoogleApi googleApi, LocationRepository locationRepository) {
        mGoogleApi = googleApi;

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

    }

    public LiveData<MyAutoCompleteData> getAutoCompleteDataLiveData() {
        return mAutoCompleteDataLiveData;
    }

    public LiveData<String> getUserInputMutableLiveData() {
        return userInputMutableLiveData;
    }

    public void setUserSearchTextQuery(String userSearchTextQuery) {
        userInputMutableLiveData.setValue(userSearchTextQuery);
    }

    public LiveData<MyAutoCompleteData> fetchAndGetAutoCompleteData(String userInput, String location) {
        MutableLiveData<MyAutoCompleteData> mutableLiveData = new MutableLiveData<>();

        Call<MyAutoCompleteData> call = mGoogleApi.getAutoCompleteData(userInput, location, 3000, "restaurant", "en", true, BuildConfig.PLACES_API_KEY);
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
