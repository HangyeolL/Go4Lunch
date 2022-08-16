package com.hangyeollee.go4lunch.repository;

import com.hangyeollee.go4lunch.api.GoogleMapsApi;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class NearBySearchDataRepositoryTest {
    private GoogleMapsApi mGoogleApi;
    private NearbySearchDataRepository mNearbySearchDataRepository;

    private MockWebServer mMockWebServer;

    @Before
    public void setUp() throws IOException {
        mGoogleApi = MyRetrofitBuilder.getGoogleMapsApi();
        mNearbySearchDataRepository = new NearbySearchDataRepository(mGoogleApi);

        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
    }

    @After
    public void shutdown() throws IOException {
        mMockWebServer.shutdown();
    }

    public void fetchDataTest() {

    }

}
