package com.hangyeollee.go4lunch.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.databinding.ListViewFragmentListItemBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.view.activities.PlaceDetailActivity;

import java.util.List;

public class ListViewFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ListViewFragmentRecyclerViewAdapter.ViewHolder> {

    private List<Result> mResultList;
    private Location mLocation;


    public ListViewFragmentRecyclerViewAdapter(List<Result> resultList, Location location) {
        mResultList = resultList;
        mLocation = location;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewFragmentListItemBinding mListViewItemBinding = ListViewFragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(mListViewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(mResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListViewFragmentListItemBinding binding;

        public ViewHolder(@NonNull ListViewFragmentListItemBinding mListViewItemBinding) {
            super(mListViewItemBinding.getRoot());
            binding = mListViewItemBinding;
        }

        public void bindViews(@NonNull Result result) {
            binding.textViewName.setText(result.getName());
            binding.textViewAddress.setText(result.getVicinity());

            if (result.getOpeningHours() != null) {
                if (result.getOpeningHours().getOpenNow()) {
                    binding.textViewIsOpenNow.setText("OPEN");
                    binding.textViewIsOpenNow.setTextColor(Color.BLUE);
                } else {
                    binding.textViewIsOpenNow.setText("CLOSED");
                    binding.textViewIsOpenNow.setTextColor(Color.RED);
                }
            }

            if (result.getRating() != null) {
                binding.ratingBar.setRating(result.getRating().floatValue());
            }

            if (result.getPhotos() != null) {
                Glide.with(itemView).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + result.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.MAPS_API_KEY).into(binding.imageViewRestaurant);
            }

            itemView.setOnClickListener(i -> {
                Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
                intent.putExtra("place id", result.getPlaceId());
                itemView.getContext().startActivity(intent);
            });

            Location restauLocation = new Location("restaurant location");
            restauLocation.setLatitude(result.getGeometry().getLocation().getLat());
            restauLocation.setLongitude(result.getGeometry().getLocation().getLng());
            float distance = mLocation.distanceTo(restauLocation);

            binding.textViewDistance.setText(String.format("%.0f", distance) + "m");
        }

    }
}