package com.hangyeollee.go4lunch.view.fragments;

import android.annotation.SuppressLint;
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
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.GoogleMapsFragmentViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

import java.util.List;

public class ListViewFragment extends Fragment {

    private String mLocation;
    String testLocation = "48.402957725552795,2.699456359957351";

    private FragmentListViewBinding binding;

    private GoogleMapsFragmentViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(GoogleMapsFragmentViewModel.class);
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);

        mViewModel.getNearBySearchLiveData("48.402957725552795,2.699456359957351").observe(getActivity(), new Observer<MyNearBySearchData>() {
            @Override
            public void onChanged(MyNearBySearchData myNearBySearchData) {
                if (myNearBySearchData != null) {
                    updateRecyclerViewList(myNearBySearchData.getResults());
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateRecyclerViewList(List<Result> resultList) {
        binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(resultList));
    }
}