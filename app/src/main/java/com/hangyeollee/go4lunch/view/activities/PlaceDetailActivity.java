package com.hangyeollee.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;

public class PlaceDetailActivity extends AppCompatActivity {

    private ActivityPlaceDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViews();

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

        if(mResult.getRating() != null) {
            binding.ratingBar.setRating(mResult.getRating().floatValue());
        }

    }

}