package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    private static final String KEY_PLACE_ID = "KEY_PLACE_ID";

    public static Intent navigate(Context context, String placeId) {
        Intent intent = new Intent(context, PlaceDetailActivity.class);
        intent.putExtra(KEY_PLACE_ID, placeId);
        return intent;
    }

    private ActivityPlaceDetailBinding binding;
    private PlaceDetailActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailActivityViewModel.class);

        PlaceDetailActivityRecyclerViewAdapter recyclerViewAdapter = new PlaceDetailActivityRecyclerViewAdapter();
        binding.recyclerViewWorkmates.setAdapter(recyclerViewAdapter);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String placeId = getIntent().getStringExtra(KEY_PLACE_ID);
        viewModel.onPlaceIdFetched(placeId);

        viewModel.getPlaceDetailActivityViewStateLiveData().observe(this,
            placeDetailActivityViewState -> bind(placeDetailActivityViewState, recyclerViewAdapter)
        );

        viewModel.getToastMessageSingleLiveEvent().observe(this,
            message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        viewModel.getIntentSingleLiveEvent().observe(this,
            intent -> startActivity(intent)
        );

    }

    private void bind(PlaceDetailActivityViewState placeDetailActivityViewState, PlaceDetailActivityRecyclerViewAdapter recyclerViewAdapter) {
        Glide.with(PlaceDetailActivity.this)
            .load(placeDetailActivityViewState.getPhotoUrl())
            .into(binding.imageViewRestaurant);

        binding.textViewName.setText(placeDetailActivityViewState.getName());
        binding.textViewAddress.setText(placeDetailActivityViewState.getAddress());
        binding.ratingBar.setRating(placeDetailActivityViewState.getRating());

        //TODO Icon color doesn't get changed !
        // TODO Hangyeol re check?
        Drawable btnLikeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_star_24, null);
        btnLikeDrawable.mutate().setColorFilter(
            ResourcesCompat.getColor(
                getResources(),
                placeDetailActivityViewState.getLikeButtonColor(),
                null
            ),
            PorterDuff.Mode.MULTIPLY
        );
        binding.buttonLike.setCompoundDrawables(null, btnLikeDrawable, null, null);

        Drawable fabDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_24, null);
        fabDrawable.mutate().setColorFilter(
            ResourcesCompat.getColor(
                getResources(),
                placeDetailActivityViewState.getFloatActButtonColor(),
                null
            ),
            PorterDuff.Mode.MULTIPLY
        );
        binding.floatingActionBtn.setImageDrawable(fabDrawable);

        binding.buttonCall.setOnClickListener(view ->
            viewModel.onButtonCallClicked(placeDetailActivityViewState)
        );

        binding.buttonWebsite.setOnClickListener(v ->
            viewModel.onButtonWebsiteClicked(placeDetailActivityViewState)
        );

        //TODO How to delete from the likedList if the restaurant is already added into ?
        binding.buttonLike.setOnClickListener(v ->
            viewModel.onButtonLikeClicked(placeDetailActivityViewState)
        );

        //TODO How to delete from the LunchRestau if the restaurant is already selected ?
        binding.floatingActionBtn.setOnClickListener(v ->
            viewModel.onFloatingActionButtonClicked(placeDetailActivityViewState)
        );

        recyclerViewAdapter.submitList(placeDetailActivityViewState.getRecyclerViewItemViewStateList());

    }

}