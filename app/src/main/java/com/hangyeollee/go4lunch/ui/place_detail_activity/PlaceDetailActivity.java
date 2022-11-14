package com.hangyeollee.go4lunch.ui.place_detail_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    private static final String KEY_PLACE_ID = "KEY_PLACE_ID";

    public static Intent navigate(Context context, String placeId) {
        Intent intent = new Intent(context, PlaceDetailActivity.class);
        intent.putExtra(KEY_PLACE_ID, placeId);
        return intent;
    }

    private ActivityPlaceDetailBinding binding;
    private PlaceDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailViewModel.class);

        PlaceDetailRecyclerViewAdapter recyclerViewAdapter = new PlaceDetailRecyclerViewAdapter();
        binding.recyclerViewWorkmates.setAdapter(recyclerViewAdapter);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String placeId = getIntent().getStringExtra(KEY_PLACE_ID);
        viewModel.onPlaceIdFetched(placeId);

        viewModel.getPlaceDetailActivityViewStateLiveData().observe(this,
                placeDetailViewState -> bind(placeDetailViewState, recyclerViewAdapter)
        );

        viewModel.getToastMessageSingleLiveEvent().observe(this,
            message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        viewModel.getIntentSingleLiveEvent().observe(this,
            intent -> startActivity(intent)
        );

    }

    private void bind(PlaceDetailViewState placeDetailViewState, PlaceDetailRecyclerViewAdapter recyclerViewAdapter) {
        Glide.with(PlaceDetailActivity.this)
            .load(placeDetailViewState.getPhotoUrl())
            .into(binding.imageViewRestaurant);

        binding.textViewName.setText(placeDetailViewState.getName());
        binding.textViewAddress.setText(placeDetailViewState.getAddress());
        binding.ratingBar.setRating(placeDetailViewState.getRating());

        //TODO Icon color doesn't get changed !
        // TODO Hangyeol re check?
        Drawable btnLikeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_star_24, null);
        btnLikeDrawable.mutate().setColorFilter(
            ResourcesCompat.getColor(
                getResources(),
                placeDetailViewState.getLikeButtonColor(),
                null
            ),
            PorterDuff.Mode.MULTIPLY
        );
        binding.buttonLike.setCompoundDrawables(null, btnLikeDrawable, null, null);

        Drawable fabDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_24, null);
        fabDrawable.mutate().setColorFilter(
            ResourcesCompat.getColor(
                getResources(),
                placeDetailViewState.getFloatActButtonColor(),
                null
            ),
            PorterDuff.Mode.MULTIPLY
        );
        binding.floatingActionBtn.setImageDrawable(fabDrawable);

        binding.buttonCall.setOnClickListener(view ->
            viewModel.onButtonCallClicked(placeDetailViewState)
        );

        binding.buttonWebsite.setOnClickListener(v ->
            viewModel.onButtonWebsiteClicked(placeDetailViewState)
        );

        //TODO How to delete from the likedList if the restaurant is already added into ?
        binding.buttonLike.setOnClickListener(v ->
            viewModel.onButtonLikeClicked(placeDetailViewState)
        );

        //TODO How to delete from the LunchRestau if the restaurant is already selected ?
        binding.floatingActionBtn.setOnClickListener(v ->
            viewModel.onFloatingActionButtonClicked(placeDetailViewState)
        );

        recyclerViewAdapter.submitList(placeDetailViewState.getRecyclerViewItemViewStateList());

    }

}