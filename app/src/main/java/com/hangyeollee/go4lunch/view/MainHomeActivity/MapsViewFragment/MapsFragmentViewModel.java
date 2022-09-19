package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;

public class MapsFragmentViewModel extends ViewModel {

    private LocationRepository mLocationRepository;
    private NearbySearchDataRepository mNearbySearchDataRepository;
    private AutoCompleteDataRepository mAutoCompleteDataRepository;

    public MapsFragmentViewModel(LocationRepository mLocationRepository, NearbySearchDataRepository mNearbySearchDataRepository, AutoCompleteDataRepository mAutoCompleteDataRepository) {
        this.mLocationRepository = mLocationRepository;
        this.mNearbySearchDataRepository = mNearbySearchDataRepository;
        this.mAutoCompleteDataRepository = mAutoCompleteDataRepository;
    }

    // Location Repo //

    public LiveData<Location> getLocationLiveData() {
        return mLocationRepository.getLocationLiveData();
    }

    // NearbySearch Repo //

    public void fetchNearBySearchData(String location) {
        mNearbySearchDataRepository.fetchData(location);
    }

    public LiveData<MyNearBySearchData> getNearBySearchLiveData() {
        return mNearbySearchDataRepository.getNearbySearchLiveData();
    }

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
