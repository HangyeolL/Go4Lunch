package com.hangyeollee.go4lunch.view.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.ActivitySettingsBinding;
import com.hangyeollee.go4lunch.viewmodel.SettingsActivityViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    private SettingsActivityViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(SettingsActivityViewModel.class);

        toolBarSetup();
        viewSetup();
    }

    private void toolBarSetup() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void viewSetup() {
        Glide.with(this).load(mViewModel.getCurrentUser().getPhotoUrl()).into(binding.imageViewUserPhoto);
        binding.textInputEditTextUserName.setText(mViewModel.getCurrentUser().getDisplayName());
        if (mViewModel.getCurrentUser().getEmail() != null) {
            binding.textInputEditTextUserEmail.setText(mViewModel.getCurrentUser().getEmail());
        } else {
            binding.textInputEditTextUserEmail.setText("not provided");
        }
    }

    private void onClickListenerSetup() {
        binding.imageViewUserPhoto.setOnClickListener(click -> {

        });

        binding.textInputEditTextUserName.setOnClickListener(click -> {
        });

        binding.buttonSave.setOnClickListener(click -> {
            mViewModel.updateUserName(binding.textInputEditTextUserName.getText().toString());
            mViewModel.updateUserPhoto(binding.imageViewUserPhoto.getDrawable().toString());
        });
    }
}