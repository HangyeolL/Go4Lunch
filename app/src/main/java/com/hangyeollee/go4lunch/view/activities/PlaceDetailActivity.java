package com.hangyeollee.go4lunch.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.GoogleMapsFragmentViewModel;

public class PlaceDetailActivity extends AppCompatActivity {

    private ActivityPlaceDetailBinding binding;

    private GoogleMapsFragmentViewModel mViewModel;

    private String mLocation;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViews();

//        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(GoogleMapsFragmentViewModel.class);
//
//        mViewModel.getLocationLiveData().observe(this, new Observer<Location>() {
//            @Override
//            public void onChanged(Location location) {
//                mLocation = location.getLatitude() + "," + location.getLongitude();
//
//                mViewModel.getNearBySearchLiveData(mLocation).observe(PlaceDetailActivity.this, new Observer<MyNearBySearchData>() {
//                    @Override
//                    public void onChanged(MyNearBySearchData myNearBySearchData) {
//                        for(int i = 0; i < myNearBySearchData.getResults().size(); i++)
//                        setUpViews(myNearBySearchData.getResults().get(i));
//                    }
//                });
//            }
//        });
    }

    private void setUpViews() {
        Intent intent = getIntent();
        Result mResult = intent.getParcelableExtra("Selected restaurant");

        if (mResult.getPhotos() != null) {
            for(int i = 0; i < mResult.getPhotos().size(); i++) {
                Glide.with(this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + mResult.getPhotos().get(i).getPhotoReference() +"&key="+ BuildConfig.MAPS_API_KEY).into(binding.imageViewRestaurant);
            }
        }

        binding.textViewName.setText(mResult.getName());
        binding.textViewAddress.setText(mResult.getVicinity());

        if (mResult.getRating() != null) {
            if (mResult.getRating() <= 2.5) {
                binding.imageViewStar3.setVisibility(View.VISIBLE);
            } else if (mResult.getRating() <= 4 && mResult.getRating() > 2.5) {
                binding.imageViewStar3.setVisibility(View.VISIBLE);
                binding.imageViewStar2.setVisibility(View.VISIBLE);
            } else if (mResult.getRating() > 4) {
                binding.imageViewStar3.setVisibility(View.VISIBLE);
                binding.imageViewStar2.setVisibility(View.VISIBLE);
                binding.imageViewStar1.setVisibility(View.VISIBLE);
            }
        }
    }

}