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
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.viewmodel.LogInAndMainActivitySharedViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

import java.util.List;

public class WorkMatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;

    private LogInAndMainActivitySharedViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(LogInAndMainActivitySharedViewModel.class);

        mViewModel.getAllUsers().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                binding.recyclerViewWorkmates.setAdapter(new WorkmatesFragmentRecyclerViewAdapter(users));
            }
        });


        return binding.getRoot();
    }
}
