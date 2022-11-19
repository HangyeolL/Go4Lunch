package com.hangyeollee.go4lunch.ui.settings_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.ActivitySettingsBinding;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;
import com.hangyeollee.go4lunch.ui.main_home_activity.MainHomeActivity;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    private SettingsViewModel viewModel;

    public static Intent navigate(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(SettingsViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewModel.getViewStateLiveData().observe(this,
                settingsViewState -> bind(settingsViewState)
        );
    }

    private void bind(SettingsViewState viewState) {
        Glide.with(this)
                .load(viewState.getPhotoUrl())
                .into(binding.imageViewUserPhoto);

        binding.textInputEditTextUserName.setText(viewState.getName());
        binding.textInputEditTextUserEmail.setText(viewState.getEmail());
        binding.switchNotification.setChecked(viewState.isSwitchBoolean());

        binding.switchNotification.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    viewModel.onSwitchClicked(isChecked);
                    Log.e("Hangyeol", "switch is checked as : " + isChecked);
                }
        );
    }

}