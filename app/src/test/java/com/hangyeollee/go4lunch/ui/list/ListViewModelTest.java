package com.hangyeollee.go4lunch.ui.list;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private LocationRepository locationRepository;
    private NearbySearchDataRepository nearbySearchDataRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;
    private FirebaseRepository firebaseRepository;

    private ListViewModel viewModel;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyNearBySearchData> nearBySearchDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyAutoCompleteData> autoCompleteDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Integer>> workmatesJoiningNumberMapMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        nearbySearchDataRepository = Mockito.mock(NearbySearchDataRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);

        Location userLocation = Mockito.mock(Location.class);
        when(userLocation.getLatitude()).thenReturn(11.12);
        when(userLocation.getLongitude()).thenReturn(11.11);

        locationMutableLiveData.setValue(userLocation);
        nearBySearchDataMutableLiveData.setValue(
                new MyNearBySearchData(
                        new ArrayList<>(), "", getNearbySearchResultList(), "OK")
        );
        autoCompleteDataMutableLiveData.setValue(
                new MyAutoCompleteData(
                        new ArrayList<>(),
                        "OK"
                )
        );
        lunchRestaurantListMutableLiveData.setValue(getLunchRestaurantList());
        workmatesJoiningNumberMapMutableLiveData.setValue(new HashMap<>());

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(nearBySearchDataMutableLiveData).when(nearbySearchDataRepository).fetchAndGetMyNearBySearchLiveData(11.12 + "," + 11.11);
        doReturn(autoCompleteDataMutableLiveData).when(autoCompleteDataRepository).getAutoCompleteDataLiveData();

        viewModel = new ListViewModel(application, locationRepository, nearbySearchDataRepository, autoCompleteDataRepository, firebaseRepository);

    }

    @Test
    public void nominal_case() {
        //WHEN
        ListViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getListViewFragmentViewStateLiveData());

        //THEN
        assertEquals(getListViewStateForTest(), viewState);
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

    private List<ListItemViewState> getItemViewStateList() {
        List<ListItemViewState> itemViewStateList = new ArrayList<>();

        itemViewStateList.add(
                new ListItemViewState(
                        "happy food",
                        "paris",
                        "OPEN",
                        1,
                        4.5f,
                        "a",
                        "placeId1",
                        "1001m",
                        1
                )
        );

        itemViewStateList.add(
                new ListItemViewState(
                        "happy food2",
                        "new york",
                        "CLOSED",
                        2,
                        3.5f,
                        "b",
                        "placeId2",
                        "1002m",
                        2
                )
        );

        itemViewStateList.add(
                new ListItemViewState(
                        "happy food3",
                        "seoul",
                        "OPEN",
                        3,
                        2.5f,
                        "c",
                        "placeId3",
                        "1003m",
                        3
                )
        );

        itemViewStateList.add(
                new ListItemViewState(
                        "happy food4",
                        "tokyo",
                        "OPEN",
                        4,
                        1.5f,
                        "d",
                        "placeId4",
                        "1004m",
                        4
                )
        );

        return itemViewStateList;
    }

    private List<LunchRestaurant> getLunchRestaurantList() {
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();

        lunchRestaurantList.add(new LunchRestaurant("placeId1,", "userA", "happy food", "2022-12-01"));
        lunchRestaurantList.add(new LunchRestaurant("placeId2,", "userB", "happy food2", "2022-12-02"));
        lunchRestaurantList.add(new LunchRestaurant("placeId3,", "userC", "happy food3", "2022-12-03"));
        lunchRestaurantList.add(new LunchRestaurant("placeId4,", "userD", "happy food4", "2022-12-04"));

        return lunchRestaurantList;
    }

    //OUTPUTS
    private ListViewState getListViewStateForTest() {
        return new ListViewState(getItemViewStateList());
    }


}
