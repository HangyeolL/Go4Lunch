package com.hangyeollee.go4lunch.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.FragmentListViewBinding;
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.MapsAndListSharedViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    private Location mUserLocation;

    private String mUserLocationToString;
    private List<Result> mNearbySearchResultList;

    private MapsAndListSharedViewModel mViewModel;

    private FragmentListViewBinding binding;

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(MapsAndListSharedViewModel.class);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.startLocationRequest();
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setMessage("Location is not authorized.\nPlease authorize location permission in settings").create().show();
        }

        //        mViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<Location>() {
        //            @Override
        //            public void onChanged(Location location) {
        //                mUserLocation = location;
        //                        mViewModel.fetchNearBySearchData(location.toString());
        //            }
        //        });

        mViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mUserLocation = location;
                mUserLocationToString = location.getLatitude() + "," + location.getLongitude();
                //                mViewModel.fetchNearBySearchData(location.toString());
            }
        });

        nearbySearchRecyclerViewSetup();
        searchViewSetup();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.stopLocationRequest();
        binding = null;
    }

    private void nearbySearchRecyclerViewSetup() {
        mViewModel.getNearBySearchLiveData().observe(getViewLifecycleOwner(), new Observer<MyNearBySearchData>() {
            @Override
            public void onChanged(MyNearBySearchData myNearBySearchData) {
                mNearbySearchResultList = myNearBySearchData.getResults();
                binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(mNearbySearchResultList, mUserLocation));
            }
        });
    }

    private void searchViewSetup() {
        SearchView searchView = getActivity().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    mViewModel.fetchAutoCompleteData(newText, mUserLocationToString);
                    autoCompleteRecyclerViewSetup();
                } else {
                    mViewModel.setAutoCompleteDataLiveDataAsNull();
                    nearbySearchRecyclerViewSetup();
                }
                return false;
            }
        });
    }

    private void autoCompleteRecyclerViewSetup() {
        mViewModel.getAutoCompleteLiveData().observe(getViewLifecycleOwner(), new Observer<MyAutoCompleteData>() {
            @Override
            public void onChanged(MyAutoCompleteData myAutoCompleteData) {
                if (myAutoCompleteData != null) {
                    List<Prediction> mPredictionList = myAutoCompleteData.getPredictions();

                    List<Result> sortedResultList = new ArrayList<>();
                    for (Result result : mNearbySearchResultList) {
                        for (Prediction prediction : mPredictionList) {
                            if (prediction.getStructuredFormatting().getMainText().contains(result.getName())) {
                                sortedResultList.add(result);
                            }
                        }
                    }
                    binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(sortedResultList, mUserLocation));
                }

            }
        });
    }

}