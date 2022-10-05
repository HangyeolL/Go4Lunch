package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.net.Uri;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;

import java.util.List;

public class MainHomeActivityViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;
    private LocationRepository mLocationRepository;
    private AutoCompleteDataRepository mAutoCompleteDataRepository;

    private MediatorLiveData<MainHomeActivityViewState> mainHomeActivityViewStateMediatorLiveData = new MediatorLiveData<>();

    private String providerId;

    public MainHomeActivityViewModel(FirebaseRepository firebaseRepository, LocationRepository locationRepository, AutoCompleteDataRepository autoCompleteDataRepository) {
        mFirebaseRepository = firebaseRepository;
        mLocationRepository = locationRepository;
        mAutoCompleteDataRepository = autoCompleteDataRepository;

        LiveData<Location> locationLiveData = mLocationRepository.getLocationLiveData();

        LiveData<MyAutoCompleteData> myAutoCompleteLiveData = Transformations.switchMap(locationLiveData, new Function<Location, LiveData<MyAutoCompleteData>>() {
            @Override
            public LiveData<MyAutoCompleteData> apply(Location location) {
                String locationToString = location.getLatitude() + "," + location.getLongitude();
                return  mAutoCompleteDataRepository.fetchAndGetAutoCompleteData(locationToString);
            }
        });

        mainHomeActivityViewStateMediatorLiveData.addSource(locationLiveData, location -> {
            combine(location, myAutoCompleteLiveData.getValue());
        });

        mainHomeActivityViewStateMediatorLiveData.addSource(myAutoCompleteLiveData, myAutoCompleteData -> {
            combine(locationLiveData.getValue(), myAutoCompleteData);
        });

    }

    private void combine(Location location, MyAutoCompleteData myAutoCompleteData) {
        if(location == null || myAutoCompleteData == null) {
            return;
        }

        String providerId = "";
        String userName = "";
        String userEmail = "";
        Uri userPhotoUrl = null;
        boolean isUserLoggedIn = false;
        List<? extends UserInfo> userInfoList = null;

        if (mFirebaseRepository.getCurrentUser() != null) {
            isUserLoggedIn = true;
            userName = mFirebaseRepository.getCurrentUser().getDisplayName();
            userEmail = mFirebaseRepository.getCurrentUser().getEmail();
            userPhotoUrl = mFirebaseRepository.getCurrentUser().getPhotoUrl();
            userInfoList = mFirebaseRepository.getCurrentUser().getProviderData();
            providerId = mFirebaseRepository.getCurrentUser().getProviderId();
        } else {
        }

        MainHomeActivityViewState mainHomeActivityViewState = new MainHomeActivityViewState(providerId, userName, userEmail, userPhotoUrl, isUserLoggedIn, userInfoList);

        mainHomeActivityViewStateMediatorLiveData.setValue(mainHomeActivityViewState);
    }

    public LiveData<MainHomeActivityViewState> getMainHomeActivityViewStateLiveData() {
        return mainHomeActivityViewStateMediatorLiveData;
    }

    public void onUserLogInEvent(String providerId) {
        this.providerId = providerId;
        mFirebaseRepository.saveUserInFirestore();
    }

    public String getProviderId() {
        return providerId;
    }

    public void onUserLogOutEvent() {
        mFirebaseRepository.signOutFromFirebaseAuth();
    }

    public void onSearchViewTextChanged(String searchViewText) {
        mAutoCompleteDataRepository.setUserSearchTextQuery(searchViewText);
    }

    /**
     * Location Repository
     */

    @SuppressLint("MissingPermission")
    public void startLocationRequest() {
        mLocationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        mLocationRepository.stopLocationRequest();
    }


}