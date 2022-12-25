package com.hangyeollee.go4lunch.ui.list;

import static com.hangyeollee.go4lunch.utils.UtilBox.resourceToUri;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteResponse;
import com.hangyeollee.go4lunch.data.model.autocomplete.PredictionResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.MyNearBySearchResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.ResultResponse;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.utils.DistanceCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ListViewModel extends ViewModel {

    private final Application context;
    private final LocationRepository locationRepository;
    private final DistanceCalculator distanceCalculator;

    private final MediatorLiveData<ListViewState> mediatorLiveData = new MediatorLiveData<>();

    public ListViewModel(
            Application context,
            LocationRepository locationRepository,
            NearbySearchDataRepository nearbySearchDataRepository,
            AutoCompleteDataRepository autoCompleteDataRepository,
            FirebaseRepository firebaseRepository,
            DistanceCalculator distanceCalculator
    ) {
        this.context = context;
        this.locationRepository = locationRepository;
        this.distanceCalculator = distanceCalculator;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<MyNearBySearchResponse> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData,
                location -> {
                    String locationToString = location.getLatitude() + "," + location.getLongitude();
                    return nearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
                }
        );

        LiveData<MyAutoCompleteResponse> myAutoCompleteDataLiveData = autoCompleteDataRepository.getAutoCompleteDataLiveData();

        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = firebaseRepository.getLunchRestaurantListOfAllUsers();

        LiveData<Map<String, Integer>> workmatesJoiningNumberMapLiveData = Transformations.switchMap(
                lunchRestaurantListLiveData,
                lunchRestaurantList -> {
                    Map<String, Integer> workmatesJoiningMap = new HashMap<>();
                    if (lunchRestaurantList != null) {
                        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                            workmatesJoiningMap.merge(lunchRestaurant.getRestaurantId(), 1, Integer::sum);
                        }
                    }
                    return new MutableLiveData<>(workmatesJoiningMap);
                }
        );

        mediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData ->
                combine(myNearBySearchData,
                        myAutoCompleteDataLiveData.getValue(),
                        workmatesJoiningNumberMapLiveData.getValue()
                )
        );

        mediatorLiveData.addSource(myAutoCompleteDataLiveData, myAutoCompleteData ->
                combine(myNearBySearchDataLiveData.getValue(),
                        myAutoCompleteData,
                        workmatesJoiningNumberMapLiveData.getValue()
                )
        );

        mediatorLiveData.addSource(workmatesJoiningNumberMapLiveData, workmatesJoiningNumberMap ->
                combine(myNearBySearchDataLiveData.getValue(),
                        myAutoCompleteDataLiveData.getValue(),
                        workmatesJoiningNumberMap
                )
        );

    }

    private void combine(@Nullable MyNearBySearchResponse myNearBySearchData,
                         @Nullable MyAutoCompleteResponse autoCompleteData,
                         @Nullable Map<String, Integer> workmatesJoiningNumberMap
    ) {
        if (myNearBySearchData == null || workmatesJoiningNumberMap == null) {
            return;
        }

        List<ListItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        boolean isOpen = false;
        float rating = 0;
        String photoReference;
        String distanceBetween = "";
        int workmatesNumber;

        if (autoCompleteData == null || autoCompleteData.getPredictions().isEmpty()) {
            for (ResultResponse resultResponse : myNearBySearchData.getResults()) {
                if (resultResponse.getOpeningHours() != null) {
                    isOpen = resultResponse.getOpeningHours().getOpenNow();
                }
                if (resultResponse.getRating() != null) {
                    rating = resultResponse.getRating().floatValue();
                }
                if (resultResponse.getPhotos() != null) {
                    photoReference =
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                                    + resultResponse.getPhotos().get(0).getPhotoReference()
                                    + "&key="
                                    + BuildConfig.PLACES_API_KEY;

                } else {
                    photoReference = resourceToUri(context, R.drawable.ic_baseline_local_dining_24);
                }

                Location currentLocation = locationRepository.getLocationLiveData().getValue();
                if (currentLocation != null) {
                    distanceBetween = distanceCalculator.distanceBetween(
                        currentLocation.getLatitude(),
                        currentLocation.getLongitude(),
                        resultResponse.getGeometry().getLocation().getLat(),
                        resultResponse.getGeometry().getLocation().getLng()
                    );
                }

                workmatesNumber = workmatesJoiningNumberMap.getOrDefault(resultResponse.getPlaceId(), 0);

                ListItemViewState recyclerViewItemViewState = new ListItemViewState(
                        resultResponse.getName(), resultResponse.getVicinity(),
                        isOpen ? context.getString(R.string.open) : context.getString(R.string.closed),
                        isOpen ? context.getColor(R.color.blue) : context.getColor(R.color.orange),
                        rating, photoReference, resultResponse.getPlaceId(), distanceBetween,
                        workmatesNumber
                );

                recyclerViewItemViewStateList.add(recyclerViewItemViewState);
            }
        } else {
            for (PredictionResponse predictionResponse : autoCompleteData.getPredictions()) {
                for (ResultResponse resultResponse : myNearBySearchData.getResults()) {
                    if (predictionResponse.getPlaceId().equals(resultResponse.getPlaceId()) &&
                            predictionResponse.getDescription().contains(resultResponse.getName())) {

                        if (resultResponse.getOpeningHours() != null) {
                            isOpen = resultResponse.getOpeningHours().getOpenNow();
                        }
                        if (resultResponse.getRating() != null) {
                            rating = resultResponse.getRating().floatValue();
                        }
                        if (resultResponse.getPhotos() != null) {
                            photoReference =
                                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                                            + resultResponse.getPhotos().get(0).getPhotoReference()
                                            + "&key="
                                            + BuildConfig.PLACES_API_KEY;

                        } else {
                            photoReference = resourceToUri(context, R.drawable.ic_baseline_local_dining_24);
                        }

                        Location currentLocation = locationRepository.getLocationLiveData().getValue();
                        if (currentLocation != null) {
                            distanceBetween = distanceCalculator.distanceBetween(
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude(),
                                resultResponse.getGeometry().getLocation().getLat(),
                                resultResponse.getGeometry().getLocation().getLng()
                            );
                        }

                        workmatesNumber = workmatesJoiningNumberMap.getOrDefault(resultResponse.getPlaceId(), 0);

                        ListItemViewState recyclerViewItemViewState = new ListItemViewState(
                                resultResponse.getName(), resultResponse.getVicinity(),
                                isOpen ? context.getString(R.string.open) : context.getString(R.string.closed),
                                isOpen ? context.getColor(R.color.blue) : context.getColor(R.color.orange),
                                rating, photoReference, resultResponse.getPlaceId(), distanceBetween,
                                workmatesNumber
                        );

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                    }
                }
            }
        }

        mediatorLiveData.setValue(new ListViewState(recyclerViewItemViewStateList));
    }

    public LiveData<ListViewState> getListViewFragmentViewStateLiveData() {
        return mediatorLiveData;
    }
}
