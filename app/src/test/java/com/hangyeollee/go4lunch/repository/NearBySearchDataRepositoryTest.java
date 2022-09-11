package com.hangyeollee.go4lunch.repository;

import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

import org.junit.Before;
import org.mockito.Mockito;

public class NearBySearchDataRepositoryTest {

    MyRetrofitBuilder mMyRetrofitBuilder = Mockito.mock(MyRetrofitBuilder.class);

    private NearbySearchDataRepository mNearbySearchDataRepository;

    @Before
    public void setUp(){
        mNearbySearchDataRepository = new NearbySearchDataRepository(mMyRetrofitBuilder.getGoogleMapsApi());
    }

    public void fetchDataTest() {

    }

}
