package com.hangyeollee.go4lunch.view.DispatcherActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.view.LogInActivity.LogInActivity;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MainHomeActivity;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class DispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No "setContentView(int)" to have a fully transparent and performant Activity
        DispatcherActivityViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DispatcherActivityViewModel.class);

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
