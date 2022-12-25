package com.hangyeollee.go4lunch.data.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.internal.Mutable;
import com.hangyeollee.go4lunch.api.GoogleApi;
import com.hangyeollee.go4lunch.data.model.autocomplete.MyAutoCompleteResponse;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class AutoCompleteDataRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GoogleApi googleApi;
    private LocationRepository locationRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;

    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> userInputStringMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {
        googleApi = Mockito.mock(GoogleApi.class);
        locationRepository = Mockito.mock(LocationRepository.class);

        Location userLocation = Mockito.mock(Location.class);
        when(userLocation.getLatitude()).thenReturn(11.12);
        when(userLocation.getLongitude()).thenReturn(11.11);
        locationMutableLiveData.setValue(userLocation);

        doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
    }

    @Ignore
    @Test
    public void nominal_case() {
        // WHEN
        autoCompleteDataRepository = new AutoCompleteDataRepository(googleApi, locationRepository);

        // THEN
        String locationToString =
            locationMutableLiveData.getValue().getLatitude()
                + ","
                + locationMutableLiveData.getValue().getLongitude();

        MyAutoCompleteResponse actualValue = LiveDataTestUtils.getValueForTesting(
            autoCompleteDataRepository.fetchAndGetAutoCompleteData(userInputStringMutableLiveData.getValue(), locationToString)
        );
        assertNotEquals(null, actualValue);
    }

    @Test
    public void edge_case_if_location_LiveData_is_null_autoCompleteLiveData_should_be_null() {
        // GIVEN
        locationMutableLiveData.setValue(null);

        // WHEN
        autoCompleteDataRepository = new AutoCompleteDataRepository(googleApi, locationRepository);

        // THEN
        assertEquals(null, LiveDataTestUtils.getValueForTesting(autoCompleteDataRepository.getAutoCompleteDataLiveData()));
    }

    @Test
    public void setUserSearchTextQuery() {
        // GIVEN
        autoCompleteDataRepository = new AutoCompleteDataRepository(googleApi, locationRepository);

        // WHEN
        autoCompleteDataRepository.setUserSearchTextQuery("ABC");

        // THEN
        String actualUserInput = LiveDataTestUtils.getValueForTesting(autoCompleteDataRepository.getUserInputMutableLiveData());
        assertEquals("ABC", actualUserInput);
    }

}