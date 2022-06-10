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
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.PlaceDetailActivityViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    private ActivityPlaceDetailBinding binding;

    private PlaceDetailActivityViewModel mViewModel;

    private Result mResult;
    private String placeId;
    private LunchRestaurant mLunchRestaurant;
    private LikedRestaurant mLikedRestaurant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(PlaceDetailActivityViewModel.class);

        toolBarSetup();

        fetchPlaceDetailData();

        mViewModel.getPlaceDetailLiveData().observe(this, new Observer<MyPlaceDetailData>() {
            @Override
            public void onChanged(MyPlaceDetailData myPlaceDetailData) {
                mResult = myPlaceDetailData.getResult();

                photoRatingTextsSetup(mResult);
                listenerSetup(mResult);
            }
        });

        mViewModel.getUsersListWithLunch(placeId).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                binding.recyclerViewWorkmates.setAdapter(new PlaceDetailActivityWorkmatesRecyclerViewAdapter(userList));
            }
        });

    }

    private void toolBarSetup() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void fetchPlaceDetailData() {
        Intent intent = getIntent();
        placeId = intent.getStringExtra("place id");

        Log.e("receivingPlaceId", placeId);

        mViewModel.fetchPlaceDetailData(placeId);
    }

    private void photoRatingTextsSetup(@Nullable Result result) {

        if (result.getPhotos() != null) {
            Glide.with(PlaceDetailActivity.this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + result.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.MAPS_API_KEY).into(binding.imageViewRestaurant);
        }

        binding.textViewName.setText(result.getName());
        binding.textViewAddress.setText(result.getVicinity());

        if (result.getRating() != null) {
            binding.ratingBar.setRating(result.getRating().floatValue());
        }
    }

    private void listenerSetup(Result result) {
        if (result.getInternationalPhoneNumber() != null) {
            binding.buttonCall.setOnClickListener(i -> {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + result.getInternationalPhoneNumber()));
                startActivity(callIntent);
            });
        } else {
            Toast.makeText(PlaceDetailActivity.this, "Couldn't find restaurant phone number", Toast.LENGTH_SHORT).show();
        }

        binding.buttonLike.setOnClickListener(listener -> {
            mLikedRestaurant = new LikedRestaurant(placeId, result.getName());

            mViewModel.setLikedRestaurant(mLikedRestaurant);

            Toast.makeText(this, result.getName() + " has added to liked restaurant list", Toast.LENGTH_SHORT).show();
        });

        String userId = mViewModel.getCurrentUser().getUid();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        mLunchRestaurant = new LunchRestaurant(placeId, userId, result.getName(), currentDate);

        binding.floatingActionBtn.setOnClickListener(listener -> {
            mViewModel.setLunchRestaurant(mLunchRestaurant);
            Toast.makeText(this, "you decided to go " + result.getName() + " for lunch", Toast.LENGTH_SHORT).show();
        });
    }
}