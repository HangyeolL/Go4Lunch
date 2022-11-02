package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

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

        String placeId = getIntent().getStringExtra("place id");
        viewModel.onPlaceIdFetched(placeId);

        viewModel.getPlaceDetailActivityViewStateLiveData().observe(this,
                placeDetailActivityViewState -> bind(placeDetailActivityViewState, recyclerViewAdapter)
        );

        viewModel.getCallButtonToastMessageSingleLiveEvent().observe(this,
                message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        viewModel.getCallButtonIntentSingleLiveEvent().observe(this,
                intent -> startActivity(intent)
        );

        viewModel.getLikeButtonToastMessageSingleLiveEvent().observe(this,
                message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        viewModel.getWebsiteButtonToastMessageSingleLiveEvent().observe(this,
                message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        viewModel.getWebsiteButtonIntentSingleLiveEvent().observe(this,
                intent -> startActivity(intent)
        );

        viewModel.getFloatingActionButtonToastMessageSingleLiveEvent().observe(this,
                message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        Drawable btnLikeDrawable = DrawableCompat.wrap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_star_24, null));
        DrawableCompat.setTint(btnLikeDrawable, getResources().getColor(placeDetailActivityViewState.getLikeButtonColor()));
        binding.buttonLike.setCompoundDrawables(null, btnLikeDrawable, null, null);

        Drawable fabDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_24, null).getConstantState().newDrawable();
        fabDrawable.mutate().setColorFilter(placeDetailActivityViewState.getFloatActButtonColor(), PorterDuff.Mode.MULTIPLY);
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