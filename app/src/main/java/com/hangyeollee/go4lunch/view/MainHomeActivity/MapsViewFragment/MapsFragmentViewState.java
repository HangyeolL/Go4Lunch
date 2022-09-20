package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;

import java.util.List;

public class MapsFragmentViewState {

    private final String userLocation;
    private final List<Result> myNearBySearchDataResultList;
    private final List<Prediction> myAutoCompleteDataPredictionList;

    public MapsFragmentViewState(String userLocation, List<Result> myNearBySearchDataResultList, List<Prediction> myAutoCompleteDataPredictionList) {
        this.userLocation = userLocation;
        this.myNearBySearchDataResultList = myNearBySearchDataResultList;
        this.myAutoCompleteDataPredictionList = myAutoCompleteDataPredictionList;
    }
}
