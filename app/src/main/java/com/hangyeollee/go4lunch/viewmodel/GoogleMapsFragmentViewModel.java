package com.hangyeollee.go4lunch.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;

public class GoogleMapsFragmentViewModel extends ViewModel {

    private NearbySearchDataRepository mNearbySearchDataRepository;
    private LocationRepository mLocationRepository;

    public MutableLiveData<Boolean> mIsLoadingMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<MyNearBySearchData> mNearBySearchMutableLiveData = new MutableLiveData<>();


    public GoogleMapsFragmentViewModel(NearbySearchDataRepository nearbySearchDataRepository, LocationRepository locationRepository) {
        mNearbySearchDataRepository = nearbySearchDataRepository;
        mLocationRepository = locationRepository;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return mIsLoadingMutableLiveData;
    }

    public LiveData<MyNearBySearchData> getNearBySearchLiveData(String location) {
        mNearBySearchMutableLiveData.setValue(mNearbySearchDataRepository.getNearbySearchLiveData(location).getValue());
        mIsLoadingMutableLiveData.postValue(mNearbySearchDataRepository.getIsLoadingLiveData().getValue());

        return mNearBySearchMutableLiveData;
    }

    public LiveData<Location> getLocationLiveData() {
        return mLocationRepository.getLocationLiveData();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        mLocationRepository.startLocationRequest();
    }

    public void stopLocationRequest() {
        mLocationRepository.stopLocationRequest();
    }

    public BitmapDescriptor makeDrawableIntoBitmap(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
