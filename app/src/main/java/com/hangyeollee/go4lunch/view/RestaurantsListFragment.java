package com.hangyeollee.go4lunch.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hangyeollee.go4lunch.databinding.FragmentRestaurantsListBinding;


public class RestaurantsListFragment extends BaseFragment<FragmentRestaurantsListBinding> {

    @Override
    FragmentRestaurantsListBinding getViewBinding() {
        return FragmentRestaurantsListBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);


    }


}