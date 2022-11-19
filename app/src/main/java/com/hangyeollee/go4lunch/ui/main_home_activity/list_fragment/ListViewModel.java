package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import static com.hangyeollee.go4lunch.utils.ResourceToUri.resourceToUri;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class ListViewModel extends ViewModel {

    private final Application context;
    private final LocationRepository locationRepository;

    private final MediatorLiveData<ListViewState> listViewFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public ListViewModel(
            Application context,
            LocationRepository locationRepository,
            NearbySearchDataRepository mNearbySearchDataRepository,
            AutoCompleteDataRepository autoCompleteDataRepository
    ) {
        this.context = context;
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

        List<ListItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

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
                    photoReference =
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                                    + result.getPhotos().get(0).getPhotoReference()
                                    + "&key="
                                    + BuildConfig.PLACES_API_KEY;

                } else {
                    photoReference = resourceToUri(context, R.drawable.ic_baseline_local_dining_24);
                }

                resultLocation.setLatitude(result.getGeometry().getLocation().getLat());
                resultLocation.setLongitude(result.getGeometry().getLocation().getLng());

                if (locationRepository.getLocationLiveData().getValue() != null) {
                    distanceBetween = String.format(Locale.getDefault(), "%.0f", locationRepository.getLocationLiveData().getValue().distanceTo(resultLocation)) + "m";
                }

                ListItemViewState recyclerViewItemViewState = new ListItemViewState(
                        result.getName(), result.getVicinity(),
                        isOpen ? context.getString(R.string.open) : context.getString(R.string.closed),
                        isOpen ? R.color.blue : R.color.orange,
                        rating, photoReference, result.getPlaceId(), distanceBetween
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
                            photoReference =
                                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                                            + result.getPhotos().get(0).getPhotoReference()
                                            + "&key="
                                            + BuildConfig.PLACES_API_KEY;

                        } else {
                            photoReference = resourceToUri(context, R.drawable.ic_baseline_local_dining_24);
                        }

                        resultLocation.setLatitude(result.getGeometry().getLocation().getLat());
                        resultLocation.setLongitude(result.getGeometry().getLocation().getLng());

                        if (locationRepository.getLocationLiveData().getValue() != null) {
                            distanceBetween = String.format(Locale.getDefault(), "%.0f", locationRepository.getLocationLiveData().getValue().distanceTo(resultLocation)) + "m";
                        }

                        ListItemViewState recyclerViewItemViewState = new ListItemViewState(
                                result.getName(), result.getVicinity(),
                                isOpen ? context.getString(R.string.open) : context.getString(R.string.closed),
                                isOpen ? R.color.blue : R.color.orange,
                                rating, photoReference, result.getPlaceId(), distanceBetween
                        );

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                    }
                }
            }
        }

        listViewFragmentViewStateMediatorLiveData.setValue(new ListViewState(recyclerViewItemViewStateList));
    }

    public LiveData<ListViewState> getListViewFragmentViewStateLiveData() {
        return listViewFragmentViewStateMediatorLiveData;
    }
}
