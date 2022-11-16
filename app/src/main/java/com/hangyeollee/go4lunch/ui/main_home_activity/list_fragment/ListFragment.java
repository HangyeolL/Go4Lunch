package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.databinding.FragmentListViewBinding;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class ListFragment extends Fragment {
    //TODO Layout file restaurant name is too long overlaps distance text!

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentListViewBinding binding = FragmentListViewBinding.inflate(inflater, container, false);

        ListViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ListViewModel.class);

        ListRecyclerViewAdapter listRecyclerViewAdapter = new ListRecyclerViewAdapter();
        binding.recyclerViewRestaurantList.setAdapter(listRecyclerViewAdapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            viewModel.getListViewFragmentViewStateLiveData().observe(getViewLifecycleOwner(),
                    listViewFragmentViewState ->
                            listRecyclerViewAdapter.submitList(listViewFragmentViewState.getListViewFragmentRecyclerViewItemViewStateList())
            );
        }

        return binding.getRoot();
    }
}