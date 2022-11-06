package com.hangyeollee.go4lunch.view.MainHomeActivity.MapViewFragment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.hangyeollee.go4lunch.MainApplication;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Geometry;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.OpeningHours;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Photo;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.LocationRepository;
import com.hangyeollee.go4lunch.repository.NearbySearchDataRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapMarkerViewState;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapsFragmentViewModel;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapsFragmentViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragmentViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private LocationRepository locationRepository;
    private NearbySearchDataRepository nearbySearchDataRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;
    private MapsFragmentViewModel viewModel;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyNearBySearchData> nearBySearchDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<MyAutoCompleteData> autoCompleteDataMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        nearbySearchDataRepository = Mockito.mock(NearbySearchDataRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);

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

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        doReturn(nearBySearchDataMutableLiveData).when(nearbySearchDataRepository).fetchAndGetMyNearBySearchLiveData(11.12 + "," + 11.11);
        doReturn(autoCompleteDataMutableLiveData).when(autoCompleteDataRepository).getAutoCompleteDataLiveData();

        viewModel = new MapsFragmentViewModel(application, locationRepository, nearbySearchDataRepository, autoCompleteDataRepository);
    }

    @Test
    public void nominal_case() {
        //WHEN
        MapsFragmentViewState mapsFragmentViewState = LiveDataTestUtils.getValueForTesting(viewModel.getMapsFragmentViewStateLiveData());

        //THEN
        assertEquals(getMapsFragmentViewStateForTest(), mapsFragmentViewState);
    }

    // INPUTS
    private List<Result> getNearbySearchResultList() {
        List<Result> nearBySearchResultList = new ArrayList<>();

        nearBySearchResultList.add(
                new Result(
                        new Geometry(new com.hangyeollee.go4lunch.model.neaerbyserachpojo.Location(11.11, 11.11)),
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
                        new Geometry(new com.hangyeollee.go4lunch.model.neaerbyserachpojo.Location(22.22, 22.22)),
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
                        new Geometry(new com.hangyeollee.go4lunch.model.neaerbyserachpojo.Location(33.33, 33.33)),
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
                        new Geometry(new com.hangyeollee.go4lunch.model.neaerbyserachpojo.Location(44.44, 44.44)),
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

    private List<MapMarkerViewState> getMapMarkerViewStateList() {
        List<MapMarkerViewState> mapMarkerViewStateList = new ArrayList<>();

        mapMarkerViewStateList.add(
                new MapMarkerViewState(
                        "placeId1",
                        new LatLng(11.11, 11.11),
                        "happy food"
                )
        );
        mapMarkerViewStateList.add(
                new MapMarkerViewState(
                        "placeId2",
                        new LatLng(22.22, 22.22),
                        "happy food2"
                )
        );
        mapMarkerViewStateList.add(
                new MapMarkerViewState(
                        "placeId3",
                        new LatLng(33.33, 33.33),
                        "happy food3"
                )
        );
        mapMarkerViewStateList.add(
                new MapMarkerViewState(
                        "placeId4",
                        new LatLng(44.44, 44.44),
                        "happy food4"
                )
        );

        return mapMarkerViewStateList;
    }

    //OUTPUTS
    private MapsFragmentViewState getMapsFragmentViewStateForTest() {
        return new MapsFragmentViewState(new LatLng(11.12, 11.11), getMapMarkerViewStateList());
    }


}
