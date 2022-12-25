package com.hangyeollee.go4lunch.ui.map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteResponse;
import com.hangyeollee.go4lunch.data.model.autocomplete.PredictionResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.GeometryResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.LocationResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.MyNearBySearchResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.OpeningHoursResponse;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.ResultResponse;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MapViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private LocationRepository locationRepository;
    private FirebaseRepository firebaseRepository;
    private NearbySearchDataRepository nearbySearchDataRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyNearBySearchResponse> nearBySearchDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyAutoCompleteResponse> autoCompleteDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();

    private MapViewModel viewModel;

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);
        nearbySearchDataRepository = Mockito.mock(NearbySearchDataRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);

        Location userLocation = Mockito.mock(Location.class);
        when(userLocation.getLatitude()).thenReturn(11.12);
        when(userLocation.getLongitude()).thenReturn(11.11);
        locationMutableLiveData.setValue(userLocation);

        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());
        nearBySearchDataMutableLiveData.setValue(
            new MyNearBySearchResponse(
                new ArrayList<>(), "", getDefaultNearbySearchResultList(), "OK"
            )
        );
        autoCompleteDataMutableLiveData.setValue(
            new MyAutoCompleteResponse(
                new ArrayList<>(),
                "OK"
            )
        );

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(nearBySearchDataMutableLiveData).when(nearbySearchDataRepository).fetchAndGetMyNearBySearchLiveData(11.12 + "," + 11.11);
        doReturn(autoCompleteDataMutableLiveData).when(autoCompleteDataRepository).getAutoCompleteDataLiveData();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();

        viewModel = new MapViewModel(application, locationRepository, firebaseRepository, nearbySearchDataRepository, autoCompleteDataRepository);
    }

    @Test
    public void nominal_case() {
        //WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //EXPECTED
        MapViewState expectedViewState = new MapViewState(new LatLng(11.12, 11.11), getDefaultMapMarkerViewStateList());

        //THEN
        assertEquals(expectedViewState, mapViewState);
    }

    @Test
    public void edge_case_autocomplete_is_null() {
        // GIVEN
        autoCompleteDataMutableLiveData.setValue(null);

        // WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        // THEN
        MapViewState expectedViewState = new MapViewState(new LatLng(11.12, 11.11), getDefaultMapMarkerViewStateList());

        assertEquals(expectedViewState, mapViewState);
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
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //THEN
        List<MapMarkerViewState> expectedMapMarkerViewStateList = new ArrayList<>();
        expectedMapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId3",
                new LatLng(33.33, 33.33),
                "happy food3",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        MapViewState expectedViewSate = new MapViewState(new LatLng(11.12, 11.11), expectedMapMarkerViewStateList);

        assertEquals(expectedViewSate, mapViewState);
    }

    @Test
    public void edge_case_placeId1_and_happy_food_is_selected_as_lunch_restaurant() {
        //GIVEN
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant("placeId1", "userA", "happy food", "2022-12-01"));
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        //WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //THEN
        List<MapMarkerViewState> expectedMapMarkerViewStateList = new ArrayList<>();
        expectedMapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId1",
                new LatLng(11.11, 11.11),
                "happy food",
                R.drawable.ic_twotone_dining_24,
                R.color.green
            )
        );
        expectedMapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId2",
                new LatLng(22.22, 22.22),
                "happy food2",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        expectedMapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId3",
                new LatLng(33.33, 33.33),
                "happy food3",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        expectedMapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId4",
                new LatLng(44.44, 44.44),
                "happy food4",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        MapViewState expectedViewSate = new MapViewState(new LatLng(11.12, 11.11), expectedMapMarkerViewStateList);

        assertEquals(expectedViewSate, mapViewState);
    }

    // DEFAULT INPUTS
    private List<ResultResponse> getDefaultNearbySearchResultList() {
        List<ResultResponse> nearBySearchResultResponseList = new ArrayList<>();

        nearBySearchResultResponseList.add(
            new ResultResponse(
                new GeometryResponse(new LocationResponse(11.11, 11.11)),
                "happy food",
                new OpeningHoursResponse(true),
                new ArrayList<>(),
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
                new ArrayList<>(),
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
                new ArrayList<>(),
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
                new ArrayList<>(),
                "placeId4",
                1.5,
                "tokyo"
            )
        );

        return nearBySearchResultResponseList;
    }

    private List<MapMarkerViewState> getDefaultMapMarkerViewStateList() {
        List<MapMarkerViewState> mapMarkerViewStateList = new ArrayList<>();

        mapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId1",
                new LatLng(11.11, 11.11),
                "happy food",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        mapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId2",
                new LatLng(22.22, 22.22),
                "happy food2",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        mapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId3",
                new LatLng(33.33, 33.33),
                "happy food3",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );
        mapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId4",
                new LatLng(44.44, 44.44),
                "happy food4",
                R.drawable.ic_twotone_dining_24,
                null
            )
        );

        return mapMarkerViewStateList;
    }

}
