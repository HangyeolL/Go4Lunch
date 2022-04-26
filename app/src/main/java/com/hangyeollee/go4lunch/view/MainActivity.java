package com.hangyeollee.go4lunch.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
import com.hangyeollee.go4lunch.view.fragments.RestaurantsListFragment;
import com.hangyeollee.go4lunch.view.fragments.WorkMatesFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    /**
     * The number of pages(Fragments)
     */
    private static final int NUM_PAGES = 3;

    /**
     * The ViewPager which handles animation and allows swiping horizontally to access previous
     */
    private ViewPager2 mViewPager2;

    /**
     * The ViewPager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter mFragmentStateAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Instantiate a ViewPager2 and a FragmentStateAdapter.
        mViewPager2 = binding.viewPager;
        mFragmentStateAdapter = new ViewPagerAdapter(this);
        mViewPager2.setAdapter(mFragmentStateAdapter);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationBar_menu_mapView).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationBar_menu_listView).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNavigationView.getMenu().findItem(R.id.bottomNavigationBar_menu_workMates).setChecked(true);
                        break;
                }
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottomNavigationBar_menu_mapView:
                mViewPager2.setCurrentItem(0);
                break;
            case R.id.bottomNavigationBar_menu_listView:
                mViewPager2.setCurrentItem(1);
                break;
            case R.id.bottomNavigationBar_menu_workMates:
                mViewPager2.setCurrentItem(2);
                break;
        }
        return true;
    }

    /**
     * ViewPagerAdapter
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
                    return new RestaurantsListFragment();
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