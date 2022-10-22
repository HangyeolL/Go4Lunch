package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class ListViewFragmentViewModel extends ViewModel {

    private final LocationRepository locationRepository;

    private final MediatorLiveData<ListViewFragmentViewState> listViewFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public ListViewFragmentViewModel(LocationRepository locationRepository, NearbySearchDataRepository mNearbySearchDataRepository, AutoCompleteDataRepository autoCompleteDataRepository) {
        this.locationRepository = locationRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<MyNearBySearchData> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData,
                location -> {
                    String locationToString = location.getLatitude() + "," + location.getLongitude();
                    return mNearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
                }
        );

        LiveData<MyAutoCompleteData> myAutoCompleteDataLiveData = autoCompleteDataRepository.getAutoCompleteDataLiveData();

        listViewFragmentViewStateMediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData ->
                combine(myNearBySearchData, myAutoCompleteDataLiveData.getValue())
        );

        listViewFragmentViewStateMediatorLiveData.addSource(myAutoCompleteDataLiveData, myAutoCompleteData ->
                combine(myNearBySearchDataLiveData.getValue(), myAutoCompleteData));

    }

    private void combine(@Nullable MyNearBySearchData myNearBySearchData, @Nullable MyAutoCompleteData autoCompleteData) {
        if (myNearBySearchData == null) {
            return;
        }

        List<ListViewFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        boolean isOpen = false;
        float rating = 0;
        String photoReference = "";
        Location resultLocation = new Location("restaurant location");
        String distanceBetween = "";

        if (autoCompleteData == null || autoCompleteData.getPredictions().isEmpty()) {
            for (Result result : myNearBySearchData.getResults()) {
                if (result.getOpeningHours() != null) {
                    isOpen = result.getOpeningHours().getOpenNow();
                }
                if (result.getRating() != null) {
                    rating = result.getRating().floatValue();
                }
                if (result.getPhotos() != null) {
                    photoReference = result.getPhotos().get(0).getPhotoReference();
                }

                resultLocation.setLatitude(result.getGeometry().getLocation().getLat());
                resultLocation.setLongitude(result.getGeometry().getLocation().getLng());

                if (locationRepository.getLocationLiveData().getValue() != null) {
                    distanceBetween = String.format(Locale.getDefault(), "%.0f", locationRepository.getLocationLiveData().getValue().distanceTo(resultLocation)) + "m";
                }

                ListViewFragmentRecyclerViewItemViewState recyclerViewItemViewState = new ListViewFragmentRecyclerViewItemViewState(
                        result.getName(), result.getVicinity(), isOpen, rating, photoReference, result.getPlaceId(), distanceBetween
                );

                recyclerViewItemViewStateList.add(recyclerViewItemViewState);
            }
        } else {
            for (Prediction prediction : autoCompleteData.getPredictions()) {
                for (Result result : myNearBySearchData.getResults()) {
                    if (prediction.getPlaceId().equals(result.getPlaceId()) &&
                            prediction.getStructuredFormatting().getMainText().contains(result.getName())) {

                        if (result.getOpeningHours() != null) {
                            isOpen = result.getOpeningHours().getOpenNow();
                        }
                        if (result.getRating() != null) {
                            rating = result.getRating().floatValue();
                        }
                        if (result.getPhotos() != null) {
                            photoReference = result.getPhotos().get(0).getPhotoReference();
                        }

                        resultLocation.setLatitude(result.getGeometry().getLocation().getLat());
                        resultLocation.setLongitude(result.getGeometry().getLocation().getLng());

                        if (locationRepository.getLocationLiveData().getValue() != null) {
                            distanceBetween = String.format(Locale.getDefault(), "%.0f", locationRepository.getLocationLiveData().getValue().distanceTo(resultLocation)) + "m";
                        }

                        ListViewFragmentRecyclerViewItemViewState recyclerViewItemViewState = new ListViewFragmentRecyclerViewItemViewState(
                                result.getName(), result.getVicinity(), isOpen, rating, photoReference, result.getPlaceId(), distanceBetween
                        );

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                    }
                }
            }
        }

        listViewFragmentViewStateMediatorLiveData.setValue(new ListViewFragmentViewState(recyclerViewItemViewStateList));
    }

    public LiveData<ListViewFragmentViewState> getListViewFragmentViewStateLiveData() {
        return listViewFragmentViewStateMediatorLiveData;
    }
}
