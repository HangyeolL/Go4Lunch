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
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Geometry;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.OpeningHours;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Result;
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
    private final MutableLiveData<MyNearBySearchData> nearBySearchDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyAutoCompleteData> autoCompleteDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutbaleLiveData = new MutableLiveData<>();

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

        lunchRestaurantListMutbaleLiveData.setValue(new ArrayList<>());
        nearBySearchDataMutableLiveData.setValue(
            new MyNearBySearchData(
                new ArrayList<>(), "", getNearbySearchResultList(), "OK"
            )
        );
        autoCompleteDataMutableLiveData.setValue(
            new MyAutoCompleteData(
                new ArrayList<>(),
                "OK"
            )
        );

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(nearBySearchDataMutableLiveData).when(nearbySearchDataRepository).fetchAndGetMyNearBySearchLiveData(11.12 + "," + 11.11);
        doReturn(autoCompleteDataMutableLiveData).when(autoCompleteDataRepository).getAutoCompleteDataLiveData();
        doReturn(lunchRestaurantListMutbaleLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();

        viewModel = new MapViewModel(application, locationRepository, firebaseRepository, nearbySearchDataRepository, autoCompleteDataRepository);
    }

    @Test
    public void nominal_case() {
        //WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //EXPECTED
        MapViewState expectedViewState = new MapViewState(new LatLng(11.12, 11.11), getMapMarkerViewStateList());

        //THEN
        assertEquals(expectedViewState, mapViewState);
    }

    @Test
    public void edge_case_autocomplete_is_null() {
        // GIVEN
        autoCompleteDataMutableLiveData.setValue(null);

        // WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //EXPECTED
        
    }

    @Test
    public void edge_case_autocomplete_matches_placeId3_and_happy_food3() {
        // GIVEN
        List<Prediction> predictions = new ArrayList<>();
        Prediction prediction = Mockito.mock(Prediction.class);
        Mockito.doReturn("placeId3").when(prediction).getPlaceId();
        Mockito.doReturn("happy food3").when(prediction).getDescription();
        predictions.add(prediction);

        autoCompleteDataMutableLiveData.setValue(
            new MyAutoCompleteData(
                predictions,
                "OK"
            )
        );

        //WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //EXPECTED
        List<MapMarkerViewState> expectedMapMarkerViewStateList = new ArrayList<>();
        expectedMapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId3",
                new LatLng(33.33, 33.33),
                "happy food3",
                R.drawable.ic_twotone_dining_24,
                R.color.green
            )
        );
        MapViewState expectedViewSate = new MapViewState(new LatLng(11.12, 11.11), expectedMapMarkerViewStateList);

        //THEN
        assertEquals(expectedViewSate, mapViewState);
    }

    @Test
    public void edge_case_selected_as_lunch_restaurant() {
        //GIVEN
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant("placeId1", "userA", "happy food", "2022-12-01"));
        lunchRestaurantListMutbaleLiveData.setValue(lunchRestaurantList);

        //WHEN
        MapViewState mapViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //EXPECTED
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

        //THEN
        assertEquals(expectedViewSate, mapViewState);
    }

    // INPUTS
    private List<Result> getNearbySearchResultList() {
        List<Result> nearBySearchResultList = new ArrayList<>();

        nearBySearchResultList.add(
            new Result(
                new Geometry(new com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Location(11.11, 11.11)),
                "happy food",
                new OpeningHours(true),
                new ArrayList<>(),
                "placeId1",
                4.5,
                "paris"
            )
        );

        nearBySearchResultList.add(
            new Result(
                new Geometry(new com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Location(22.22, 22.22)),
                "happy food2",
                new OpeningHours(true),
                new ArrayList<>(),
                "placeId2",
                3.5,
                "new york"
            )
        );

        nearBySearchResultList.add(
            new Result(
                new Geometry(new com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Location(33.33, 33.33)),
                "happy food3",
                new OpeningHours(true),
                new ArrayList<>(),
                "placeId3",
                2.5,
                "seoul"
            )
        );

        nearBySearchResultList.add(
            new Result(
                new Geometry(new com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Location(44.44, 44.44)),
                "happy food4",
                new OpeningHours(true),
                new ArrayList<>(),
                "placeId4",
                1.5,
                "tokyo"
            )
        );

        return nearBySearchResultList;
    }

    private List<LunchRestaurant> getLunchRestaurantList() {
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();

        lunchRestaurantList.add(new LunchRestaurant("placeId1", "userA", "happy food", "2022-12-01"));
        lunchRestaurantList.add(new LunchRestaurant("placeId2", "userB", "happy food2", "2022-12-02"));
        lunchRestaurantList.add(new LunchRestaurant("placeId3", "userC", "happy food3", "2022-12-03"));
        lunchRestaurantList.add(new LunchRestaurant("placeId4", "userD", "happy food4", "2022-12-04"));

        return lunchRestaurantList;
    }

    private List<MapMarkerViewState> getMapMarkerViewStateList() {
        List<MapMarkerViewState> mapMarkerViewStateList = new ArrayList<>();

        mapMarkerViewStateList.add(
            new MapMarkerViewState(
                "placeId1",
                new LatLng(11.11, 11.11),
                "happy food",
                R.drawable.ic_twotone_dining_24,
                R.color.green
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
                R.color.green
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
