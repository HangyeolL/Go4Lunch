package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

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
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class ListViewFragment extends Fragment {

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentListViewBinding binding = FragmentListViewBinding.inflate(inflater, container, false);

        ListViewFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ListViewFragmentViewModel.class);

        ListViewFragmentRecyclerViewAdapter listViewFragmentRecyclerViewAdapter = new ListViewFragmentRecyclerViewAdapter();
        binding.recyclerViewRestaurantList.setAdapter(listViewFragmentRecyclerViewAdapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            viewModel.getListViewFragmentViewStateLiveData().observe(getViewLifecycleOwner(),
                    listViewFragmentViewState ->
                            listViewFragmentRecyclerViewAdapter.submitList(listViewFragmentViewState.getListViewFragmentRecyclerViewItemViewStateList())
            );
        }

        return binding.getRoot();
    }
}