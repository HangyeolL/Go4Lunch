package com.hangyeollee.go4lunch.view.activities;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityMainBinding;
import com.hangyeollee.go4lunch.view.fragments.GoogleMapsFragment;
import com.hangyeollee.go4lunch.view.fragments.ListViewFragment;
import com.hangyeollee.go4lunch.view.fragments.SettingsFragment;
import com.hangyeollee.go4lunch.view.fragments.WorkMatesFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    private LocationManager mLocationManager;

    private static final int NUM_PAGES = 3;
    private FragmentStateAdapter mFragmentStateAdapter;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPagerSetup();
        navigationViewSetup();
        linkDrawerLayoutWithToolbar();
        bottomNavigationBarSetup();

    }

    private void viewPagerSetup() {
        mFragmentStateAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(mFragmentStateAdapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationView_menu_mapView).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationView_menu_listView).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationView_menu_workMates).setChecked(true);
                        break;
                    case 4:
                        binding.NavigationView.getMenu().findItem(R.id.navigationView_settings).setChecked(true);
                }
            }
        });
    }

    private void navigationViewSetup() {
        binding.NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigationView_yourLunch:

                        break;
                    case R.id.navigationView_settings:
                        binding.viewPager.setCurrentItem(4);
                        break;
                    case R.id.navigationView_logout:
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
                case 3:
                    break;
                case 4:
                    return new SettingsFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}