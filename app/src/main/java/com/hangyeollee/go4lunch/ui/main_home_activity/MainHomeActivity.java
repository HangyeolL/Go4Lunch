package com.hangyeollee.go4lunch.ui.main_home_activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityMainHomeBinding;
import com.hangyeollee.go4lunch.databinding.MainActivityHeaderNavigationViewBinding;
import com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment.ListFragment;
import com.hangyeollee.go4lunch.ui.main_home_activity.map_fragment.MapFragment;
import com.hangyeollee.go4lunch.ui.main_home_activity.workmates_fragment.WorkmatesFragment;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class MainHomeActivity extends AppCompatActivity {

    private ActivityMainHomeBinding binding;

    private MainHomeViewModel viewModel;

    public static Intent navigate(Context context) {
        return new Intent(context, MainHomeActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainActivityHeaderNavigationViewBinding navigationViewHeaderBinding = MainActivityHeaderNavigationViewBinding.bind(binding.NavigationView.getHeaderView(0));

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainHomeViewModel.class);

        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        viewModel.startLocationRequest();
                    } else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainHomeActivity.this);
                        alertBuilder.setMessage("Location is not authorized.\nPlease authorize location permission in settings")
                                .create()
                                .show();
                    }
                });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "is already granted");
            viewModel.startLocationRequest();
        } else {
            Log.d("Permission", "is not granted launch permission dialog");
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        viewModel.onUserLoggedIn();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, MapFragment.newInstance()).commit();
        }

        linkDrawerLayoutWithToolbar();
        bottomNavigationBarSetup();
        searchViewSetup();

        viewModel.getMainHomeActivityViewStateLiveData().observe(this, mainHomeViewState -> {
                    navigationViewHeaderBinding.textViewUserName.setText(mainHomeViewState.getUserName());
                    navigationViewHeaderBinding.textViewUserEmail.setText(mainHomeViewState.getUserEmail());

                    Glide.with(navigationViewHeaderBinding.imageViewUserPhoto)
                            .load(mainHomeViewState.getUserPhotoUrl())
                            .into(navigationViewHeaderBinding.imageViewUserPhoto);

                    setNavigationViewItemSelectedListener(mainHomeViewState);
                }
        );

        viewModel.getToastMessageSingleLiveEvent().observe(this,
                message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        viewModel.getIntentSingleLiveEvent().observe(this,
                intent -> {
                    startActivity(intent);
                }
        );

    }

    private void linkDrawerLayoutWithToolbar() {
        setSupportActionBar(binding.toolBar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void bottomNavigationBarSetup() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottomNavigationView_menu_mapView:
                    binding.toolBar.setTitle(R.string.i_am_hungry);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, MapFragment.newInstance())
                            .commitNow();
                    break;
                case R.id.bottomNavigationView_menu_listView:
                    binding.toolBar.setTitle(R.string.i_am_hungry);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, ListFragment.newInstance())
                            .commitNow();
                    break;
                case R.id.bottomNavigationView_menu_workMates:
                    binding.toolBar.setTitle(R.string.available_workmates);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, WorkmatesFragment.newInstance())
                            .commitNow();
                    break;
            }
            return true;
        });
    }

    private void searchViewSetup() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.onSearchViewTextChanged(newText);
                return false;
            }
        });
    }

    private void setNavigationViewItemSelectedListener(MainHomeViewState mainHomeViewState) {
        binding.NavigationView.setNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.navigationView_yourLunch:
                            viewModel.onYourLunchClicked(mainHomeViewState);
                            break;
                        case R.id.navigationView_settings:
                            viewModel.onSettingsClicked();
                            break;
                        case R.id.navigationView_logout:
                            viewModel.onLogOutClicked();
                            break;
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
        );
    }

    //TODO should work on this
    @Override
    public void onBackPressed() {
        int FragmentId = 0;
        switch (FragmentId) {
            case R.id.bottomNavigationView_menu_mapView:
                if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    super.onBackPressed();
                } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }
            case R.id.bottomNavigationView_menu_listView:
                binding.bottomNavigationView.setSelectedItemId(R.id.bottomNavigationView_menu_mapView);
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }
            case R.id.bottomNavigationView_menu_workMates:
                binding.bottomNavigationView.setSelectedItemId(R.id.bottomNavigationView_menu_listView);
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.stopLocationRequest();
        binding = null;
    }
}