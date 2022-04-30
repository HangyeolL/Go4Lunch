package com.hangyeollee.go4lunch.repository;

import com.hangyeollee.go4lunch.api.NearbySearchApi;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;

import retrofit2.Call;
import retrofit2.http.Query;

public class NearBySearchRepository {

    private NearbySearchApi mNearbySearchApi;

    public NearBySearchRepository(NearbySearchApi nearbySearchApi) {
        mNearbySearchApi = nearbySearchApi;
    }

    public void getNearbySearchData() {
        mNearbySearchApi.getNearbySearchData();
    }

}
