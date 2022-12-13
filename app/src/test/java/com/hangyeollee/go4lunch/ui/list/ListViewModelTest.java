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

import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.data.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Geometry;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.OpeningHours;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Photo;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.LocationRepository;
import com.hangyeollee.go4lunch.data.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.utils.DistanceCalculator;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Ignore;
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

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyNearBySearchData> nearBySearchDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyAutoCompleteData> autoCompleteDataMutableLiveData = new MutableLiveData<>();
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

        Location userLocation = Mockito.mock(Location.class);
        when(userLocation.getLatitude()).thenReturn(DEFAULT_USER_LAT);
        when(userLocation.getLongitude()).thenReturn(DEFAULT_USER_LONG);
        locationMutableLiveData.setValue(userLocation);

        nearBySearchDataMutableLiveData.setValue(
            new MyNearBySearchData(
                new ArrayList<>(), "", getDefaultNearbySearchResultList(), "OK")
        );
        autoCompleteDataMutableLiveData.setValue(
            new MyAutoCompleteData(
                new ArrayList<>(),
                "OK"
            )
        );
        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());
        workmatesJoiningNumberMapMutableLiveData.setValue(new HashMap<>());

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(nearBySearchDataMutableLiveData).when(nearbySearchDataRepository).fetchAndGetMyNearBySearchLiveData(DEFAULT_USER_LAT + "," + DEFAULT_USER_LONG);
        doReturn(autoCompleteDataMutableLiveData).when(autoCompleteDataRepository).getAutoCompleteDataLiveData();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();
        doReturn("distanceBetween").when(distanceCalculator).distanceBetween(
            anyDouble(),
            anyDouble(),
            anyDouble(),
            anyDouble()
        );

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

    @Ignore // TODO FIXME
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
        ListViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getListViewFragmentViewStateLiveData());

        //EXPECTED
        List<ListItemViewState> expectedItemViewStateList = new ArrayList<>();
        expectedItemViewStateList.add(
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
        ListViewState expectedViewState = new ListViewState(expectedItemViewStateList);

        //THEN
        assertEquals(expectedViewState, viewState);

    }

    // INPUTS
    private List<Result> getDefaultNearbySearchResultList() {
        List<Result> nearBySearchResultList = new ArrayList<>();

        Photo photo = Mockito.mock(Photo.class);
        doReturn("photo").when(photo).getPhotoReference();
        doReturn(250).when(photo).getHeight();
        doReturn(250).when(photo).getWidth();
        doReturn(new ArrayList<>()).when(photo).getHtmlAttributions();

        nearBySearchResultList.add(
            new Result(
                new Geometry(new com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.Location(11.11, 11.11)),
                "happy food",
                new OpeningHours(true),
                Collections.singletonList(photo),
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
                Collections.singletonList(photo),
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
                Collections.singletonList(photo),
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
                Collections.singletonList(photo),
                "placeId4",
                1.5,
                "tokyo"
            )
        );

        return nearBySearchResultList;
    }

    private List<ListItemViewState> getDefaultItemViewStateList() {
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
                "distanceBetween",
                1
            )
        );

        itemViewStateList.add(
            new ListItemViewState(
                "happy food2",
                "new york",
                "OPEN",
                2,
                3.5f,
                "b",
                "placeId2",
                "distanceBetween",
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
                "distanceBetween",
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
                "distanceBetween",
                4
            )
        );

        return itemViewStateList;
    }

}
