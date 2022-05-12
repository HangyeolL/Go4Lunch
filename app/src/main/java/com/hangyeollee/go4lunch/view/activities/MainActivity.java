package com.hangyeollee.go4lunch.view.activities;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationBarView;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityMainBinding;
import com.hangyeollee.go4lunch.view.fragments.GoogleMapsFragment;
import com.hangyeollee.go4lunch.view.fragments.ListViewFragment;
import com.hangyeollee.go4lunch.view.fragments.WorkMatesFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

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
//
//        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Handles the user's response to the system permissions dialog.
//        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//            if (isGranted) {
//                // Permission is granted now
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//            } else {
//                // Explain to the user that the feature is unavailable because the
//                // features requires a permission that the user has denied. At the
//                // same time, respect the user's decision. Don't link to system
//                // settings in an effort to convince the user to change their
//                // decision.
//                //Todo : Alert Dialog builder
//            }
//        });
//
//        // Launch requestPermissionLauncher if mLocation permission is not already granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
//        } else {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        }

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
                }
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (binding.viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * On Item Selected Listener
     */
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