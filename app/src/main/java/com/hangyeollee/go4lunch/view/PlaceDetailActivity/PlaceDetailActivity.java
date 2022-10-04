package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;
import com.hangyeollee.go4lunch.utility.MyCalendar;
import com.hangyeollee.go4lunch.utility.MySharedPreferenceUtil;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    private ActivityPlaceDetailBinding binding;

    private PlaceDetailActivityViewModel mViewModel;

    private PlaceDetailActivityWorkmatesRecyclerViewAdapter mAdapter;

    private String placeId;
    private LunchRestaurant mLunchRestaurant;
    private LikedRestaurant mLikedRestaurant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailActivityViewModel.class);

        mAdapter = new PlaceDetailActivityWorkmatesRecyclerViewAdapter();

        toolBarSetup();
        fetchPlaceDetailData();
        mViewModel.fetchMediatorLivedata();

        mViewModel.getMediatorLiveData().observe(this, new Observer<PlaceDetailActivityViewState>() {
            @Override
            public void onChanged(PlaceDetailActivityViewState placeDetailActivityViewState) {
                basicViewSetup(placeDetailActivityViewState);
                listenerSetup(placeDetailActivityViewState);

                mAdapter.submitList(placeDetailActivityViewState.getUserList());
                binding.recyclerViewWorkmates.setAdapter(mAdapter);

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

    private void basicViewSetup(PlaceDetailActivityViewState placeDetailActivityViewState) {
        Result result = placeDetailActivityViewState.getResult();

        if (result.getPhotos() != null) {
            Glide.with(PlaceDetailActivity.this)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + result
                            .getPhotos()
                            .get(0)
                            .getPhotoReference() + "&key=" + BuildConfig.PLACES_API_KEY)
                    .into(binding.imageViewRestaurant);
        }

        binding.textViewName.setText(result.getName());
        binding.textViewAddress.setText(result.getVicinity());

        if (result.getRating() != null) {
            binding.ratingBar.setRating(result.getRating().floatValue());
        }

        Drawable btnLikeDrawable = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_baseline_star_24));

        if (placeDetailActivityViewState.getSelectedAsLikedRestaurant()) {
            DrawableCompat.setTint(btnLikeDrawable, getResources().getColor(R.color.orange));
        } else {
            DrawableCompat.setTint(btnLikeDrawable, getResources().getColor(R.color.white));
        }
        binding.buttonLike.setCompoundDrawables(null, btnLikeDrawable, null, null);

        Drawable fabDrawable = getResources().getDrawable(R.drawable.ic_baseline_check_24).getConstantState().newDrawable();

        if (placeDetailActivityViewState.getSelectedAsLunchRestaurant()) {
            fabDrawable.mutate().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);
        } else {
            fabDrawable.mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        }
        binding.floatingActionBtn.setImageDrawable(fabDrawable);
    }

    private void listenerSetup(PlaceDetailActivityViewState placeDetailActivityViewState) {
        Result result = placeDetailActivityViewState.getResult();

        if (result.getInternationalPhoneNumber() != null) {
            binding.buttonCall.setOnClickListener(i -> {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + result.getInternationalPhoneNumber()));
                startActivity(callIntent);
            });
        } else {
            binding.buttonCall.setOnClickListener(i -> {
                Toast.makeText(PlaceDetailActivity.this, "Couldn't find restaurant phone number", Toast.LENGTH_SHORT)
                        .show();
            });
        }

        if (result.getWebsite() != null) {
            binding.buttonLike.setOnClickListener(i -> {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(result.getWebsite()));
                startActivity(websiteIntent);
            });
        } else {
            binding.buttonLike.setOnClickListener(i -> {
                Toast.makeText(this, "Website not available", Toast.LENGTH_SHORT).show();
            });
        }

        binding.buttonLike.setOnClickListener(listener -> {
            if (placeDetailActivityViewState.getSelectedAsLikedRestaurant()) {
                Toast.makeText(this, result.getName() + "is already liked", Toast.LENGTH_SHORT).show();
            } else {
                mLikedRestaurant = new LikedRestaurant(placeId, result.getName());
                mViewModel.setLikedRestaurant(mLikedRestaurant);

                Toast.makeText(this, result.getName() + " has added to liked restaurant list", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        binding.floatingActionBtn.setOnClickListener(listener -> {
            if (placeDetailActivityViewState.getSelectedAsLunchRestaurant()) {
                Toast.makeText(this, "You already decided to go " + result.getName(), Toast.LENGTH_SHORT).show();
            } else {
                mLunchRestaurant = new LunchRestaurant(placeId, mViewModel.getCurrentUser().getUid(), result.getName(), MyCalendar.getCurrentDate());

                mViewModel.setLunchRestaurant(mLunchRestaurant);

                Toast.makeText(this, "you will go to " + result.getName() + " for lunch", Toast.LENGTH_SHORT)
                        .show();

                SharedPreferences.Editor mSharedPrefEditor = new MySharedPreferenceUtil(this).getInstanceOfEditor();

                mSharedPrefEditor.putString("LunchRestaurant", mLunchRestaurant.getName());
                mSharedPrefEditor.commit();
            }

        });
    }

}