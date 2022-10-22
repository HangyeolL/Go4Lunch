package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityMainHomeBinding;
import com.hangyeollee.go4lunch.databinding.MainActivityHeaderNavigationViewBinding;
import com.hangyeollee.go4lunch.utility.MySharedPreferenceUtil;
import com.hangyeollee.go4lunch.view.LogInActivity.LogInActivity;
import com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment.ListViewFragment;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapsFragment;
import com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment.WorkmatesFragment;
import com.hangyeollee.go4lunch.view.SettingsActivity.SettingsActivity;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class MainHomeActivity extends AppCompatActivity {

    private ActivityMainHomeBinding binding;

    private MainHomeActivityViewModel mViewModel;

    private SharedPreferences mSharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainActivityHeaderNavigationViewBinding navigationViewHeaderBinding = MainActivityHeaderNavigationViewBinding.bind(binding.NavigationView.getHeaderView(0));

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainHomeActivityViewModel.class);

        mSharedPref = new MySharedPreferenceUtil(this).getInstanceOfSharedPref();

        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        mViewModel.startLocationRequest();
                    } else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainHomeActivity.this);
                        alertBuilder.setMessage("Location is not authorized.\nPlease authorize location permission in settings").create().show();
                    }
                });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "is already granted");
            mViewModel.startLocationRequest();
        } else {
            Log.d("Permission", "is not granted launch permission dialog");
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

//        alarmSetup();
        navigationViewItemSelectedListener();
        linkDrawerLayoutWithToolbar();
        bottomNavigationBarSetup();
        searchViewSetup();

        mViewModel.getMainHomeActivityViewStateLiveData().observe(this, mainHomeActivityViewState -> {
            if (mainHomeActivityViewState.isUserLoggedIn()) {
                mViewModel.onUserLogInEvent(mainHomeActivityViewState.getProviderId());

                navigationViewHeaderBinding.textViewUserName.setText(mainHomeActivityViewState.getUserName());

                if (mainHomeActivityViewState.getUserEmail() != null) {
                    navigationViewHeaderBinding.textViewUserEmail.setText(mainHomeActivityViewState.getUserEmail());
                } else {
                    navigationViewHeaderBinding.textViewUserEmail.setText(R.string.email_unavailable);
                }

                if (mainHomeActivityViewState.getUserPhotoUrl() != null) {
                    Glide.with(MainHomeActivity.this).load(mainHomeActivityViewState.getUserPhotoUrl()).into(navigationViewHeaderBinding.imageViewUserPhoto);
                } else {
                    Glide.with(MainHomeActivity.this).load(R.drawable.ic_baseline_person_outline_24).into(navigationViewHeaderBinding.imageViewUserPhoto);
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, MapsFragment.newInstance()).commit();
        }

    }

//    private void alarmSetup() {
//
//        if (mSharedPref.getBoolean("Notification boolean", true)) {
//
//            AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 12);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//
//            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//            PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
//
//            Log.e("TimeInMillisNow", calendar.getTimeInMillis() + "");
//
//            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarmIntent);
//
//        } else {
//
//        }
//    }

    private void navigationViewItemSelectedListener() {
        binding.NavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigationView_yourLunch:
                    Toast.makeText(MainHomeActivity.this, mSharedPref.getString("LunchRestaurant", ""), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.navigationView_settings:
                    startActivity(new Intent(MainHomeActivity.this, SettingsActivity.class));
                    break;
                case R.id.navigationView_logout:
                    signOutAccordingToProviders(mViewModel.getProviderId());
                    mViewModel.onUserLogOutEvent();
                    startActivity(new Intent(MainHomeActivity.this, LogInActivity.class));
                    finish();
                    break;
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void linkDrawerLayoutWithToolbar() {
        setSupportActionBar(binding.toolBar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void bottomNavigationBarSetup() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottomNavigationView_menu_mapView:
                    binding.toolBar.setTitle("I am Hungry!");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, MapsFragment.newInstance())
                            .commitNow();
                    break;
                case R.id.bottomNavigationView_menu_listView:
                    binding.toolBar.setTitle("I am Hungry!");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, ListViewFragment.newInstance())
                            .commitNow();
                    break;
                case R.id.bottomNavigationView_menu_workMates:
                    binding.toolBar.setTitle("Available Workmates");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, WorkmatesFragment.newInstance())
                            .commitNow();
                    break;
            }
            return true;
        });
    }

    private void signOutAccordingToProviders(String providerId) {
        switch (providerId) {
            case "google.com":
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                GoogleSignInClient mSignInClient = GoogleSignIn.getClient(this, gso);
                mSignInClient.signOut();
                break;
            case "facebook.com":
                LoginManager.getInstance().logOut();
                break;
        }
    }

    private void searchViewSetup() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mViewModel.onSearchViewTextChanged(newText);
                return false;
            }
        });
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
        mViewModel.stopLocationRequest();
        binding = null;
    }
}