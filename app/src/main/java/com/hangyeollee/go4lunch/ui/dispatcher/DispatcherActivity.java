package com.hangyeollee.go4lunch.ui.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hangyeollee.go4lunch.ui.ViewModelFactory;
import com.hangyeollee.go4lunch.ui.login.LogInActivity;
import com.hangyeollee.go4lunch.ui.main_home.MainHomeActivity;

public class DispatcherActivity extends AppCompatActivity {

    public static Intent navigate(Context context) {
        return new Intent(context, DispatcherActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No "setContentView(int)" to have a fully transparent and performant Activity
        DispatcherViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DispatcherViewModel.class);

        viewModel.getViewActionSingleLiveEvent().observe(this, dispatcherViewAction -> {
            switch (dispatcherViewAction) {
                case GO_TO_LOGIN_SCREEN:
                    startActivity(LogInActivity.navigate(this));
                    finish();
                    break;
                case GO_TO_MAIN_HOME_SCREEN:
                    startActivity(MainHomeActivity.navigate(this));
                    finish();
                    break;
            }
        });
    }
}
