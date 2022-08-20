package com.hangyeollee.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.databinding.FragmentWorkmatesBinding;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.viewmodel.FirebaseViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

import java.util.List;

public class WorkMatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;

    private FirebaseViewModel mViewModel;

    private WorkmatesFragmentRecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(FirebaseViewModel.class);

        mAdapter = new WorkmatesFragmentRecyclerViewAdapter();

        mViewModel.getUsersList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mAdapter.updateUserLists(users);
            }
        });

        mViewModel.getLunchRestaurantListOfAllUsers().observe(getViewLifecycleOwner(), new Observer<List<LunchRestaurant>>() {
            @Override
            public void onChanged(List<LunchRestaurant> lunchRestaurantList) {
                mAdapter.updateRestaurantList(lunchRestaurantList);
            }
        });

        binding.recyclerViewWorkmates.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
