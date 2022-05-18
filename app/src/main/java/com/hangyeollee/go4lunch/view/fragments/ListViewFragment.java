package com.hangyeollee.go4lunch.view.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.databinding.FragmentListViewBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.viewmodel.GoogleMapsFragmentViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

public class ListViewFragment extends Fragment {

    private String mLocation;

    private FragmentListViewBinding binding;

    private GoogleMapsFragmentViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(GoogleMapsFragmentViewModel.class);

        mViewModel.startLocationRequest();

        mViewModel.getLocationLiveData().observe(getActivity(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                String mLocation = location.getLatitude() + "," + location.getLongitude();

                mViewModel.getNearBySearchLiveData(mLocation).observe(getActivity(), new Observer<MyNearBySearchData>() {
                    @Override
                    public void onChanged(MyNearBySearchData myNearBySearchData) {
                        binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(myNearBySearchData.getResults(), location));
                    }
                });
            }
        });

        mViewModel.getIsLoadingLiveData().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
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