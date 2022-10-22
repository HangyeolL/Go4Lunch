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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.model.LikedRestaurant;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.utility.MyCalendar;
import com.hangyeollee.go4lunch.utility.MySharedPreferenceUtil;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    private ActivityPlaceDetailBinding binding;
    private PlaceDetailActivityViewModel viewModel;

    private String placeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailActivityViewModel.class);

        PlaceDetailActivityRecyclerViewAdapter recyclerViewAdapter = new PlaceDetailActivityRecyclerViewAdapter();
        binding.recyclerViewWorkmates.setAdapter(recyclerViewAdapter);

        toolBarSetup();
        fetchPlaceDetailData();

        viewModel.getMediatorLiveData().observe(this, placeDetailActivityViewState -> {
            basicViewSetup(placeDetailActivityViewState);
            listenerSetup(placeDetailActivityViewState);
            recyclerViewAdapter.submitList(placeDetailActivityViewState.getRecyclerViewItemViewStateList());
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

        viewModel.requestPlaceDetailData(placeId);
    }

    private void basicViewSetup(PlaceDetailActivityViewState placeDetailActivityViewState) {
        Glide.with(PlaceDetailActivity.this)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" +
                        placeDetailActivityViewState.getPhotoUrl() + "&key=" + BuildConfig.PLACES_API_KEY)
                .into(binding.imageViewRestaurant);

        binding.textViewName.setText(placeDetailActivityViewState.getName());
        binding.textViewAddress.setText(placeDetailActivityViewState.getAddress());
        binding.ratingBar.setRating(placeDetailActivityViewState.getRating());

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
        if (!placeDetailActivityViewState.getInternationalPhoneNumber().equalsIgnoreCase(getString(R.string.international_phone_number_unavailable))) {
            binding.buttonCall.setOnClickListener(i -> {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + placeDetailActivityViewState.getInternationalPhoneNumber()));
                startActivity(callIntent);
            });
        } else {
            binding.buttonCall.setOnClickListener(i -> Toast.makeText(PlaceDetailActivity.this, R.string.international_phone_number_unavailable, Toast.LENGTH_SHORT).show());
        }

        if (!placeDetailActivityViewState.getWebsite().equalsIgnoreCase(getString(R.string.website_unavailable))) {
            binding.buttonLike.setOnClickListener(i -> {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(placeDetailActivityViewState.getWebsite()));
                startActivity(websiteIntent);
            });
        } else {
            binding.buttonLike.setOnClickListener(i -> Toast.makeText(this, R.string.website_unavailable, Toast.LENGTH_SHORT).show());
        }

        binding.buttonLike.setOnClickListener(listener -> {
            if (placeDetailActivityViewState.getSelectedAsLikedRestaurant()) {
                Toast.makeText(this, placeDetailActivityViewState.getName() + getString(R.string.is_already_liked), Toast.LENGTH_SHORT).show();
            } else {
                LikedRestaurant likedRestaurant = new LikedRestaurant(placeId, placeDetailActivityViewState.getName());
                viewModel.onSetLikedRestaurantButtonClicked(likedRestaurant);

                Toast.makeText(this, placeDetailActivityViewState.getName() + getString(R.string.add_to_liked_restaurant_list), Toast.LENGTH_SHORT).show();
            }
        });

        binding.floatingActionBtn.setOnClickListener(listener -> {
            if (placeDetailActivityViewState.getSelectedAsLunchRestaurant()) {
                Toast.makeText(this, getString(R.string.you_already_decided_to_go) + placeDetailActivityViewState.getName(), Toast.LENGTH_SHORT).show();
            } else {
                LunchRestaurant lunchRestaurant = new LunchRestaurant(placeId, viewModel.getCurrentUser().getUid(), placeDetailActivityViewState.getName(), MyCalendar.getCurrentDate());
                viewModel.onSetLunchRestaurantButtonClicked(lunchRestaurant);

                Toast.makeText(this, getString(R.string.you_will_go_to) + placeDetailActivityViewState.getName() + getString(R.string.for_lunch), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor mSharedPrefEditor = new MySharedPreferenceUtil(this).getInstanceOfEditor();

                mSharedPrefEditor.putString("LunchRestaurant", lunchRestaurant.getRestaurantMame());
                mSharedPrefEditor.commit();
            }
        });
    }

}