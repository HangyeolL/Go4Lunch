package com.hangyeollee.go4lunch.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.databinding.FragmentListViewBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.viewmodel.MapsAndListSharedViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

public class ListViewFragment extends Fragment {

    private String mUserLocationInString;
    private Location mUserLocation;

    private MapsAndListSharedViewModel mViewModel;

    private FragmentListViewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(MapsAndListSharedViewModel.class);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.startLocationRequest();
        } else {

        }

        mViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mUserLocation = location;
                mUserLocationInString = location.getLatitude() + "," + location.getLongitude();
            }
        });

        mViewModel.getNearBySearchLiveData().observe(getViewLifecycleOwner(), new Observer<MyNearBySearchData>() {
            @Override
            public void onChanged(MyNearBySearchData myNearBySearchData) {
                binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(myNearBySearchData.getResults(), mUserLocation));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.stopLocationRequest();
        binding = null;
    }

}