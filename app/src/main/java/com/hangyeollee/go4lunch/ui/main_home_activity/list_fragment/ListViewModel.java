package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import static com.hangyeollee.go4lunch.utils.UtilBox.resourceToUri;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.User;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class ListViewModel extends ViewModel {

    private final Application context;
    private final LocationRepository locationRepository;

    private final MediatorLiveData<ListViewState> mediatorLiveData = new MediatorLiveData<>();

    public ListViewModel(
            Application context,
            LocationRepository locationRepository,
            NearbySearchDataRepository nearbySearchDataRepository,
            AutoCompleteDataRepository autoCompleteDataRepository,
            FirebaseRepository firebaseRepository
    ) {
        this.context = context;
        this.locationRepository = locationRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        LiveData<MyNearBySearchData> myNearBySearchDataLiveData = Transformations.switchMap(locationLiveData,
                location -> {
                    String locationToString = location.getLatitude() + "," + location.getLongitude();
                    return nearbySearchDataRepository.fetchAndGetMyNearBySearchLiveData(locationToString);
                }
        );

        LiveData<MyAutoCompleteData> myAutoCompleteDataLiveData = autoCompleteDataRepository.getAutoCompleteDataLiveData();

        LiveData<List<User>> userListLiveData = firebaseRepository.getUsersList();
        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = firebaseRepository.getLunchRestaurantListOfAllUsers();

        mediatorLiveData.addSource(myNearBySearchDataLiveData, myNearBySearchData ->
                combine(myNearBySearchData,
                        myAutoCompleteDataLiveData.getValue(),
                        userListLiveData.getValue(),
                        lunchRestaurantListLiveData.getValue()
                )
        );

        mediatorLiveData.addSource(myAutoCompleteDataLiveData, myAutoCompleteData ->
                combine(myNearBySearchDataLiveData.getValue(),
                        myAutoCompleteData,
                        userListLiveData.getValue(),
                        lunchRestaurantListLiveData.getValue()
                )
        );

        mediatorLiveData.addSource(userListLiveData, userList ->
                combine(myNearBySearchDataLiveData.getValue(),
                        myAutoCompleteDataLiveData.getValue(),
                        userList,
                        lunchRestaurantListLiveData.getValue()
                )
        );

        mediatorLiveData.addSource(lunchRestaurantListLiveData, lunchRestaurantList ->
                combine(myNearBySearchDataLiveData.getValue(),
                        myAutoCompleteDataLiveData.getValue(),
                        userListLiveData.getValue(),
                        lunchRestaurantList
                )
        );

    }

    private void combine(@Nullable MyNearBySearchData myNearBySearchData,
                         @Nullable MyAutoCompleteData autoCompleteData,
                         @Nullable List<User> userList,
                         @Nullable List<LunchRestaurant> lunchRestaurantList
    ) {
        if (myNearBySearchData == null) {
            return;
        }

        List<ListItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        boolean isOpen = false;
        float rating = 0;
        String photoReference;
        Location resultLocation = new Location("restaurant location");
        String distanceBetween = "";

        List<User> workmatesJoiningList = new ArrayList<>();

        for (User user : userList) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (user.getId().equalsIgnoreCase(lunchRestaurant.getUserId())) {
                    workmatesJoiningList.add(user);
                }
            }
        }

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
                        rating, photoReference, result.getPlaceId(), distanceBetween,
                        workmatesJoiningList.size()
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
                                rating, photoReference, result.getPlaceId(), distanceBetween,
                                workmatesJoiningList.size()
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
