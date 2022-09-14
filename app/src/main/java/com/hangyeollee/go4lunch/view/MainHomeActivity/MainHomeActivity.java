package com.hangyeollee.go4lunch.view.MainHomeActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityMainBinding;
import com.hangyeollee.go4lunch.databinding.MainActivityHeaderNavigationViewBinding;
import com.hangyeollee.go4lunch.utility.AlarmReceiver;
import com.hangyeollee.go4lunch.utility.MySharedPreferenceUtil;
import com.hangyeollee.go4lunch.view.LogInActivity.LogInActivity;
import com.hangyeollee.go4lunch.view.SettingsActivity.SettingsActivity;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment.MapsFragment;
import com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment.ListViewFragment;
import com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment.WorkMatesFragment;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

import java.util.Calendar;
import java.util.List;

public class MainHomeActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainHomeActivityViewModel mViewModel;

    private static final int NUM_PAGES = 3;

    private SharedPreferences mSharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainHomeActivityViewModel.class);

        mSharedPref = new MySharedPreferenceUtil(this).getInstanceOfSharedPref();

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher, as an instance variable.
        // Permission is granted. Continue the action or workflow in your
        // app.
        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onActivityResult(Boolean isGranted) {
                if (isGranted) {
                    mViewModel.startLocationRequest();
                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainHomeActivity.this);
                    alertBuilder.setMessage("Location is not authorized.\nPlease authorize location permission in settings").create().show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "is already granted");
            mViewModel.startLocationRequest();
        } else {
            Log.d("Permission", "is not granted launch permission dialog");
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        alarmSetup();
        createLoggedInUserInFirestore();
        viewPagerSetup();
        navigationViewUserProfileSetup();
        navigationViewItemSelectedListener();
        linkDrawerLayoutWithToolbar();
        bottomNavigationBarSetup();
    }

    private void alarmSetup() {

        if (mSharedPref.getBoolean("Notification boolean", true)) {

            AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

            Log.e("TimeInMillisNow", calendar.getTimeInMillis() + "");

            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarmIntent);

        } else {

        }
    }

    private void createLoggedInUserInFirestore() {
        if (mViewModel.getCurrentUser() != null) {
            mViewModel.saveUserInFirestore();
        }
    }

    private void viewPagerSetup() {
        FragmentStateAdapter mFragmentStateAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(mFragmentStateAdapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.toolBar.setTitle("I am Hungry!");
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationView_menu_mapView).setChecked(true);
                        break;
                    case 1:
                        binding.toolBar.setTitle("I am Hungry!");
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationView_menu_listView).setChecked(true);
                        break;
                    case 2:
                        binding.toolBar.setTitle("Available Workmates");
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationView_menu_workMates).setChecked(true);
                        break;
                }
            }
        });
    }

    private void navigationViewUserProfileSetup() {
        MainActivityHeaderNavigationViewBinding navigationViewHeaderBinding = MainActivityHeaderNavigationViewBinding.bind(binding.NavigationView.getHeaderView(0));

        FirebaseUser currentUser = mViewModel.getCurrentUser();

        if (currentUser != null) {
            List<? extends UserInfo> userInfoList = currentUser.getProviderData();

            navigationViewHeaderBinding.textViewUserName.setText(userInfoList.get(0).getDisplayName());
            navigationViewHeaderBinding.textViewUserEmail.setText(userInfoList.get(0).getEmail());

            if (mViewModel.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this).load(userInfoList.get(0).getPhotoUrl()).into(navigationViewHeaderBinding.imageViewUserPhoto);
            } else {
                navigationViewHeaderBinding.imageViewUserPhoto.setImageResource(R.drawable.ic_baseline_person_outline_24);
            }
        }
    }

    private void signOutAccordingToProviders() {
        switch (mViewModel.getCurrentUser().getProviderId()) {
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

    private void navigationViewItemSelectedListener() {
        binding.NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigationView_yourLunch:
                        Toast.makeText(MainHomeActivity.this,mSharedPref.getString("LunchRestaurant", ""), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigationView_settings:
                        startActivity(new Intent(MainHomeActivity.this, SettingsActivity.class));
                        break;
                    case R.id.navigationView_logout:
                        signOutAccordingToProviders();
                        mViewModel.signOutFromFirebaseAuth();
                        startActivity(new Intent(MainHomeActivity.this, LogInActivity.class));
                        finish();
                        break;
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void linkDrawerLayoutWithToolbar() {
        setSupportActionBar(binding.toolBar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void bottomNavigationBarSetup() {
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNavigationView_menu_mapView:
                        binding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.bottomNavigationView_menu_listView:
                        binding.viewPager.setCurrentItem(1);
                        break;
                    case R.id.bottomNavigationView_menu_workMates:
                        binding.viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.viewPager.getCurrentItem() == 0 && !binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();

        } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

        } else if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.stopLocationRequest();
    }

    /**
     * ViewPagerAdapter for MainHomeActivity
     */
    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new MapsFragment();
                case 1:
                    return new ListViewFragment();
                case 2:
                    return new WorkMatesFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}