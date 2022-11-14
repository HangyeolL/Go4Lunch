package com.hangyeollee.go4lunch.ui.settings_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.databinding.ActivitySettingsBinding;
import com.hangyeollee.go4lunch.utils.MySharedPreferenceUtil;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    private SettingsViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(SettingsViewModel.class);

        toolBarSetup();
        viewSetup();
        switchSetup(this);
    }

    private void toolBarSetup() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void viewSetup() {
//        Glide.with(this)
//                .load(mViewModel.getCurrentUser().getPhotoUrl())
//                .into(binding.imageViewUserPhoto);
//        binding.textInputEditTextUserName.setText(mViewModel.getCurrentUser().getDisplayName());
//
//            binding.textInputEditTextUserEmail.setText(mViewModel.getCurrentUser().getEmail());
//
//            binding.textInputEditTextUserEmail.setText("not provided");

    }

    private void switchSetup(Context context) {
        SharedPreferences mSharedPref = new MySharedPreferenceUtil(this).getInstanceOfSharedPref();

        if (mSharedPref.getBoolean("Notification boolean", true)) {
            binding.switchNotification.setChecked(true);
        } else {
            binding.switchNotification.setChecked(false);
        }

        binding.switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor mEditor = new MySharedPreferenceUtil(context).getInstanceOfEditor();

                if (buttonView.isChecked()) {
                    Log.e("switch", "is checked : " + isChecked);
                    mEditor.putBoolean("Notification boolean", true);
                    mEditor.commit();
                } else {
                    Log.e("switch", "is checked : " + isChecked);
                    mEditor.putBoolean("Notification boolean", false);
                    mEditor.commit();
                }
            }
        });
    }
}