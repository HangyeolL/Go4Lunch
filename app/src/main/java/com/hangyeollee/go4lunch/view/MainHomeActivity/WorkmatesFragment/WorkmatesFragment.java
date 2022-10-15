package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

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
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;

    private WorkmatesFragmentViewModel mViewModel;

    private WorkmatesFragmentRecyclerViewAdapter mAdapter;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmatesFragmentViewModel.class);

        mAdapter = new WorkmatesFragmentRecyclerViewAdapter();
        binding.recyclerViewWorkmates.setAdapter(mAdapter);

        mViewModel.getWorkmatesFragmentViewStateLiveData().observe(getViewLifecycleOwner(), new Observer<WorkmatesFragmentViewState>() {
            @Override
            public void onChanged(WorkmatesFragmentViewState workmatesFragmentViewState) {
                mAdapter.submitList(workmatesFragmentViewState.getRecyclerViewItemViewStateList());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
