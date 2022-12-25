package com.hangyeollee.go4lunch.data.repository;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteDataRepository {

    private final GoogleApi mGoogleApi;
    private final LiveData<MyAutoCompleteResponse> autoCompleteDataLiveData;
    private final MutableLiveData<String> userInputMutableLiveData = new MutableLiveData<>();

    public AutoCompleteDataRepository(GoogleApi googleApi, LocationRepository locationRepository) {
        mGoogleApi = googleApi;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        autoCompleteDataLiveData = Transformations.switchMap(
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

    public LiveData<MyAutoCompleteResponse> getAutoCompleteDataLiveData() {
        return autoCompleteDataLiveData;
    }

    public LiveData<String> getUserInputMutableLiveData() {
        return userInputMutableLiveData;
    }

    public void setUserSearchTextQuery(String userSearchTextQuery) {
        userInputMutableLiveData.setValue(userSearchTextQuery);
    }

    public LiveData<MyAutoCompleteResponse> fetchAndGetAutoCompleteData(String userInput, String location) {
        MutableLiveData<MyAutoCompleteResponse> mutableLiveData = new MutableLiveData<>();

        Call<MyAutoCompleteResponse> call = mGoogleApi.getAutoCompleteData(userInput, location, 3000, "restaurant", "en", true, BuildConfig.PLACES_API_KEY);
        call.enqueue(new Callback<MyAutoCompleteResponse>() {
            @Override
            public void onResponse(Call<MyAutoCompleteResponse> call, Response<MyAutoCompleteResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MyAutoCompleteResponse> call, Throwable t) {
                Log.w("AutoComplete", "failed", t);
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }
}
