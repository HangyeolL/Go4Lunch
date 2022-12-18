package com.hangyeollee.go4lunch.data.repository;

import com.hangyeollee.go4lunch.utils.MyRetrofitBuilder;

import org.junit.Before;
import org.mockito.Mockito;

public class NearBySearchDataRepositoryTest {

    private MyRetrofitBuilder myRetrofitBuilder;
    private NearbySearchDataRepository nearbySearchDataRepository;

    @Before
    public void setUp() {
        myRetrofitBuilder = Mockito.mock(MyRetrofitBuilder.class);
        nearbySearchDataRepository = new NearbySearchDataRepository(myRetrofitBuilder.getGoogleApi());
    }

    public void fetchDataNearbySearchDataTest() {

    }

}
