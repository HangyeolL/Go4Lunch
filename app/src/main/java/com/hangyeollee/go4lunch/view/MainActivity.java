package com.hangyeollee.go4lunch.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.hangyeollee.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    /**
     * The number of pages
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
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate a ViewPager2 and a FragmentStateAdapter.
        mViewPager2 = binding.viewPager;
        mFragmentStateAdapter = new ScreenSlidePagerAdapter(this);

        mViewPager2.setAdapter(mFragmentStateAdapter);
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

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0 :
                    return new GoogleMapsFragment();
                case 1 :
                    return new RestaurantsListFragment();
                case 2 :
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