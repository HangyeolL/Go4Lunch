package com.hangyeollee.go4lunch.view.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.hangyeollee.go4lunch.view.fragments.GoogleMapsFragment;
import com.hangyeollee.go4lunch.view.fragments.ListViewFragment;
import com.hangyeollee.go4lunch.view.fragments.WorkMatesFragment;
import com.hangyeollee.go4lunch.viewmodel.MainActivityViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainActivityViewModel mViewModel;

    private GoogleSignInClient mSignInClient;

    private static final int NUM_PAGES = 3;
    private FragmentStateAdapter mFragmentStateAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainActivityViewModel.class);

        alarmSetup();
        createLoggedInUserInFirestore();
        viewPagerSetup();
        navigationViewUserProfileSetup();
        navigationViewItemSelectedListener();
        linkDrawerLayoutWithToolbar();
        bottomNavigationBarSetup();
    }

    private void alarmSetup() {
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND, 00);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarmIntent);
    }

    private void createLoggedInUserInFirestore() {
        if (mViewModel.getCurrentUser() != null) {
            mViewModel.saveUserInFirestore();
        }
    }

    private void viewPagerSetup() {
        mFragmentStateAdapter = new ViewPagerAdapter(this);
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
                mSignInClient = GoogleSignIn.getClient(this, gso);
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
                        break;
                    case R.id.navigationView_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    case R.id.navigationView_logout:
                        signOutAccordingToProviders();
                        mViewModel.signOutFromFirebaseAuth();
                        startActivity(new Intent(MainActivity.this, LogInActivity.class));
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


    /**
     * ViewPagerAdapter for MainActivity
     */
    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new GoogleMapsFragment();
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