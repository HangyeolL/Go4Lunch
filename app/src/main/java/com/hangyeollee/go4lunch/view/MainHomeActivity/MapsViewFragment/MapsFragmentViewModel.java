package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MapsFragmentViewModel extends ViewModel {

    private LocationRepository mLocationRepository;
    private NearbySearchDataRepository mNearbySearchDataRepository;
    private AutoCompleteDataRepository mAutoCompleteDataRepository;

    private MediatorLiveData<MapsFragmentViewState> mapsFragmentViewStateMediatorLiveData = new MediatorLiveData<>();


    public MapsFragmentViewModel(LocationRepository mLocationRepository, NearbySearchDataRepository mNearbySearchDataRepository, AutoCompleteDataRepository mAutoCompleteDataRepository) {
        this.mLocationRepository = mLocationRepository;
        this.mNearbySearchDataRepository = mNearbySearchDataRepository;
        this.mAutoCompleteDataRepository = mAutoCompleteDataRepository;

        LiveData<Location> locationLiveData = mLocationRepository.getLocationLiveData();

        LiveData<MyNearBySearchData> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData, new Function<Location, LiveData<MyNearBySearchData>>() {
            @Override
            public LiveData<MyNearBySearchData> apply(Location location) {
                String locationToString = location.getLatitude() + "," + location.getLongitude();
                return  mNearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
            }
        });

        mapsFragmentViewStateMediatorLiveData.addSource(locationLiveData, location -> {
            combine(location, myNearBySearchDataLiveData.getValue());
        });

        mapsFragmentViewStateMediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData -> {
            combine(locationLiveData.getValue(), myNearBySearchData);
        });

    }

    private void combine(@Nullable Location location, @Nullable MyNearBySearchData myNearBySearchData) {
        if(location == null || myNearBySearchData == null) {
            return;
        }

        String userLocation = null;
        LatLng userLatLng = null;

        if(location != null) {
            userLocation = location.getLatitude() + "," + location.getLongitude();
            userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }

        List<Result> nearBySearchResultList = new ArrayList<>();

        for(Result result : myNearBySearchData.getResults()) {
            nearBySearchResultList.add(result);
        }

        mapsFragmentViewStateMediatorLiveData.setValue(
                new MapsFragmentViewState(userLocation, userLatLng, nearBySearchResultList, false)
        );
    }

    // Mediator //
    public LiveData<MapsFragmentViewState> getMapsFragmentViewStateMediatorLiveData() {
        return mapsFragmentViewStateMediatorLiveData;
    }

    // Location Repo //

    public LiveData<Location> getLocationLiveData() {
        return mLocationRepository.getLocationLiveData();
    }

    // NearbySearch Repo //

    // AutoComplete Repo //

    public LiveData<MyAutoCompleteData> getAutoCompleteLiveData() {
        return mAutoCompleteDataRepository.getAutoCompleteLiveData();
    }

    public void setAutoCompleteDataLiveDataAsNull() {
        mAutoCompleteDataRepository.setAutoCompleteDataLiveDataAsNull();
    }

    public void fetchAutoCompleteData(String input, String location) {
        mAutoCompleteDataRepository.fetchData(input, location);
    }

    // Etc.... //

    public BitmapDescriptor makeDrawableIntoBitmap(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
