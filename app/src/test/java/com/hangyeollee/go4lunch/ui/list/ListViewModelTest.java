package com.hangyeollee.go4lunch.ui.list;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteResponse;
import com.hangyeollee.go4lunch.data.model.autocomplete.PredictionResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.GeometryResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.LocationResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.MyNearBySearchResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.OpeningHoursResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.PhotoResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.ResultResponse;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.utils.DistanceCalculator;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final Double DEFAULT_USER_LAT = 11.12;
    private static final Double DEFAULT_USER_LONG = 11.11;

    private Application application;
    private LocationRepository locationRepository;
    private NearbySearchDataRepository nearbySearchDataRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;
    private FirebaseRepository firebaseRepository;
    private DistanceCalculator distanceCalculator;

    private OpeningHoursResponse openingHoursResponse;
    private PhotoResponse photoResponse;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyNearBySearchResponse> nearBySearchDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyAutoCompleteResponse> autoCompleteDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Integer>> workmatesJoiningNumberMapMutableLiveData = new MutableLiveData<>();

    private ListViewModel viewModel;

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        nearbySearchDataRepository = Mockito.mock(NearbySearchDataRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);

        distanceCalculator = Mockito.mock(DistanceCalculator.class);
        ResultResponse nearbySearchResultResponse = Mockito.mock(ResultResponse.class);
        openingHoursResponse = Mockito.mock(OpeningHoursResponse.class);

        Location userLocation = Mockito.mock(Location.class);
        when(userLocation.getLatitude()).thenReturn(DEFAULT_USER_LAT);
        when(userLocation.getLongitude()).thenReturn(DEFAULT_USER_LONG);
        locationMutableLiveData.setValue(userLocation);

        nearBySearchDataMutableLiveData.setValue(
            new MyNearBySearchResponse(
                new ArrayList<>(), "", getDefaultNearbySearchResultList(), "OK")
        );
        autoCompleteDataMutableLiveData.setValue(
            new MyAutoCompleteResponse(
                new ArrayList<>(),
                "OK"
            )
        );
        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());
        workmatesJoiningNumberMapMutableLiveData.setValue(new HashMap<>());

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(nearBySearchDataMutableLiveData)
            .when(nearbySearchDataRepository).fetchAndGetMyNearBySearchLiveData(DEFAULT_USER_LAT + "," + DEFAULT_USER_LONG);
        doReturn(autoCompleteDataMutableLiveData).when(autoCompleteDataRepository).getAutoCompleteDataLiveData();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();

        doReturn("distanceBetween").when(distanceCalculator).distanceBetween(
            anyDouble(),
            anyDouble(),
            anyDouble(),
            anyDouble()
        );
        doReturn(openingHoursResponse).when(nearbySearchResultResponse).getOpeningHours();
        doReturn("OPEN").when(application).getString(R.string.open);
        doReturn("CLOSED").when(application).getString(R.string.closed);
        doReturn(R.color.blue).when(application).getColor(R.color.blue);
        doReturn(R.color.orange).when(application).getColor(R.color.orange);

        viewModel = new ListViewModel(
            application,
            locationRepository,
            nearbySearchDataRepository,
            autoCompleteDataRepository,
            firebaseRepository,
            distanceCalculator
        );
    }

    @Test
    public void nominal_case() {
        //WHEN
        ListViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getListViewFragmentViewStateLiveData());

        //THEN
        ListViewState expectedListViewState = new ListViewState(getDefaultItemViewStateList());

        assertEquals(expectedListViewState, viewState);
    }

    @Test
    public void edge_case_autoComplete_is_null() {
        //GIVEN
        autoCompleteDataMutableLiveData.setValue(null);

        //WHEN
        ListViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getListViewFragmentViewStateLiveData());

        //THEN
        ListViewState expectedListViewState = new ListViewState(getDefaultItemViewStateList());

        assertEquals(expectedListViewState, viewState);
    }

    @Test
    public void edge_case_autocomplete_matches_placeId3_and_happy_food3() {
        // GIVEN
        List<PredictionResponse> predictionResponses = new ArrayList<>();
        PredictionResponse predictionResponse = Mockito.mock(PredictionResponse.class);
        Mockito.doReturn("placeId3").when(predictionResponse).getPlaceId();
        Mockito.doReturn("happy food3").when(predictionResponse).getDescription();
        predictionResponses.add(predictionResponse);

        autoCompleteDataMutableLiveData.setValue(
            new MyAutoCompleteResponse(
                predictionResponses,
                "OK"
            )
        );

        //WHEN
        ListViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getListViewFragmentViewStateLiveData());

        //THEN
        List<ListItemViewState> expectedItemViewStateList = new ArrayList<>();
        expectedItemViewStateList.add(
            new ListItemViewState(
                "happy food3",
                "seoul",
                "OPEN",
                R.color.blue,
                2.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId3",
                "distanceBetween",
                0
            )
        );
        ListViewState expectedViewState = new ListViewState(expectedItemViewStateList);

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_placeId1_and_happy_food_is_selected_as_lunch_restaurant() {
        //GIVEN
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant("placeId1", "userA", "happy food", "2022-12-01"));
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        //WHEN
        ListViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getListViewFragmentViewStateLiveData());

        //THEN
        List<ListItemViewState> expectedItemViewStateList = new ArrayList<>();
        doReturn(true).when(openingHoursResponse).getOpenNow();
        expectedItemViewStateList.add(
            new ListItemViewState(
                "happy food",
                "paris",
                "OPEN",
                R.color.blue,
                4.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId1",
                "distanceBetween",
                1
            )
        );

        expectedItemViewStateList.add(
            new ListItemViewState(
                "happy food2",
                "new york",
                "OPEN",
                R.color.blue,
                3.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId2",
                "distanceBetween",
                0
            )
        );

        expectedItemViewStateList.add(
            new ListItemViewState(
                "happy food3",
                "seoul",
                "OPEN",
                R.color.blue,
                2.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId3",
                "distanceBetween",
                0
            )
        );

        expectedItemViewStateList.add(
            new ListItemViewState(
                "happy food4",
                "tokyo",
                "OPEN",
                R.color.blue,
                1.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId4",
                "distanceBetween",
                0
            )
        );
        ListViewState expectedListViewState = new ListViewState(expectedItemViewStateList);

        assertEquals(expectedListViewState, viewState);
    }

    // INPUTS
    private List<ResultResponse> getDefaultNearbySearchResultList() {
        List<ResultResponse> nearBySearchResultResponseList = new ArrayList<>();

        photoResponse = Mockito.mock(PhotoResponse.class);
        doReturn("photoResponse").when(photoResponse).getPhotoReference();
        doReturn(250).when(photoResponse).getHeight();
        doReturn(250).when(photoResponse).getWidth();
        doReturn(new ArrayList<>()).when(photoResponse).getHtmlAttributions();

        nearBySearchResultResponseList.add(
            new ResultResponse(
                new GeometryResponse(new LocationResponse(11.11, 11.11)),
                "happy food",
                new OpeningHoursResponse(true),
                Collections.singletonList(photoResponse),
                "placeId1",
                4.5,
                "paris"
            )
        );

        nearBySearchResultResponseList.add(
            new ResultResponse(
                new GeometryResponse(new LocationResponse(22.22, 22.22)),
                "happy food2",
                new OpeningHoursResponse(true),
                Collections.singletonList(photoResponse),
                "placeId2",
                3.5,
                "new york"
            )
        );

        nearBySearchResultResponseList.add(
            new ResultResponse(
                new GeometryResponse(new LocationResponse(33.33, 33.33)),
                "happy food3",
                new OpeningHoursResponse(true),
                Collections.singletonList(photoResponse),
                "placeId3",
                2.5,
                "seoul"
            )
        );

        nearBySearchResultResponseList.add(
            new ResultResponse(
                new GeometryResponse(new LocationResponse(44.44, 44.44)),
                "happy food4",
                new OpeningHoursResponse(true),
                Collections.singletonList(photoResponse),
                "placeId4",
                1.5,
                "tokyo"
            )
        );

        return nearBySearchResultResponseList;
    }

    private List<ListItemViewState> getDefaultItemViewStateList() {
        List<ListItemViewState> itemViewStateList = new ArrayList<>();

        doReturn(true).when(openingHoursResponse).getOpenNow();

        itemViewStateList.add(
            new ListItemViewState(
                "happy food",
                "paris",
                "OPEN",
                R.color.blue,
                4.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId1",
                "distanceBetween",
                0
            )
        );

        itemViewStateList.add(
            new ListItemViewState(
                "happy food2",
                "new york",
                "OPEN",
                R.color.blue,
                3.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId2",
                "distanceBetween",
                0
            )
        );

        itemViewStateList.add(
            new ListItemViewState(
                "happy food3",
                "seoul",
                "OPEN",
                R.color.blue,
                2.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId3",
                "distanceBetween",
                0
            )
        );

        itemViewStateList.add(
            new ListItemViewState(
                "happy food4",
                "tokyo",
                "OPEN",
                R.color.blue,
                1.5f,
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
                "placeId4",
                "distanceBetween",
                0
            )
        );

        return itemViewStateList;
    }

}
