package com.hangyeollee.go4lunch.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.PlaceDetailActivityViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    private ActivityPlaceDetailBinding binding;

    private PlaceDetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(PlaceDetailActivityViewModel.class);
        setUpViews();

    }



    private void setUpViews() {
        Intent intent = getIntent();
        String placeId = intent.getStringExtra("place id");
        Log.i("Place id" , intent.getStringExtra("place id"));

        mViewModel.fetchPlaceDetailData(placeId);
        mViewModel.getPlaceDetailLiveData().observe(this, new Observer<MyPlaceDetailData>() {
            @Override
            public void onChanged(MyPlaceDetailData myPlaceDetailData) {
                Result mResult = myPlaceDetailData.getResult();

                if (mResult.getPhotos() != null) {
                    for (int i = 0; i < mResult.getPhotos().size(); i++) {
                        Glide.with(PlaceDetailActivity.this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + mResult.getPhotos().get(i).getPhotoReference() + "&key=" + BuildConfig.MAPS_API_KEY).into(binding.imageViewRestaurant);
                    }
                }

                binding.textViewName.setText(mResult.getName());
                binding.textViewAddress.setText(mResult.getVicinity());

                if (mResult.getRating() != null) {
                    binding.ratingBar.setRating(mResult.getRating().floatValue());
                }

                if (mResult.getInternationalPhoneNumber() != null) {
                    binding.buttonCall.setOnClickListener(i -> {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse(mResult.getInternationalPhoneNumber()));
                        startActivity(callIntent);
                    });
                } else {
                    Toast.makeText(PlaceDetailActivity.this, "Couldn't find restaurant phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}