package com.hangyeollee.go4lunch.ui.dispatcher_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.ui.logIn_activity.LogInActivity;
import com.hangyeollee.go4lunch.ui.main_home_activity.MainHomeActivity;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class DispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No "setContentView(int)" to have a fully transparent and performant Activity
        DispatcherViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DispatcherViewModel.class);

        viewModel.getViewActionSingleLiveEvent().observe(this, dispatcherViewAction -> {
            switch (dispatcherViewAction) {
                case GO_TO_LOGIN_SCREEN:
                    startActivity(new Intent(DispatcherActivity.this, LogInActivity.class));
                    finish();
                    break;
                case GO_TO_MAIN_HOME_SCREEN:
                    startActivity(new Intent(DispatcherActivity.this, MainHomeActivity.class));
                    finish();
                    break;
            }
        });
    }
}
