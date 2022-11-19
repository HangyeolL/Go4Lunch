package com.hangyeollee.go4lunch.ui.logIn_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityLogInBinding;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class LogInActivity extends AppCompatActivity {

    public static Intent navigate(Context context) {
        return new Intent(context, LogInActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLogInBinding binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        LogInViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(LogInViewModel.class);

        Drawable facebookLogo = AppCompatResources.getDrawable(this, R.drawable.ic_facebook_logo);
        binding.ButtonFacebookLogIn.setCompoundDrawablesWithIntrinsicBounds(facebookLogo, null, null, null);
        Drawable googleLogo = AppCompatResources.getDrawable(this, R.drawable.ic_google_logo);
        binding.ButtonGoogleLogIn.setCompoundDrawablesWithIntrinsicBounds(googleLogo, null, null, null);

        // Retrieve data from googleLogIn()
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> viewModel.onGoogleLogInResult(result));

        binding.ButtonGoogleLogIn.setOnClickListener(v ->
                activityResultLauncher.launch(viewModel.onButtonGoogleLogInClicked())
        );

        binding.ButtonFacebookLogIn.setOnClickListener(v ->
                viewModel.onButtonFacebookLogInClicked(this)
        );

        viewModel.onFacebookLogInResult();

        viewModel.getIntentSingleLiveEvent().observe(this, intent -> {
                    startActivity(intent);
                    finish();
                }
        );

        viewModel.getStringSingleLiveEvent().observe(this, message -> {
                    Snackbar snackbar = Snackbar.make(binding.constraintLayoutParentLayout, message, BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                }
        );

    }

}