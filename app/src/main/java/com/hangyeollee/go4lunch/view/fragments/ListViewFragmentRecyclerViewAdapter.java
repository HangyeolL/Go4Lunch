package com.hangyeollee.go4lunch.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.databinding.ListViewItemBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.utility.DistanceCalculator;
import com.hangyeollee.go4lunch.view.activities.PlaceDetailActivity;

import java.util.List;

public class ListViewFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ListViewFragmentRecyclerViewAdapter.ViewHolder> {

    private List<Result> mResultList;


    public ListViewFragmentRecyclerViewAdapter(List<Result> resultList) {
        mResultList = resultList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListViewItemBinding binding;

        public ViewHolder(@NonNull ListViewItemBinding mListViewItemBinding) {
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

            if(result.getRating() != null) {
                binding.ratingBar.setRating(result.getRating().floatValue());
            }

            if (result.getPhotos() != null) {
                for(int i = 0; i < result.getPhotos().size(); i++) {
                    Glide.with(itemView).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + result.getPhotos().get(i).getPhotoReference() +"&key="+ BuildConfig.MAPS_API_KEY).into(binding.imageViewRestaurant);
                }
            }

            itemView.setOnClickListener(i -> {
                Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
                intent.putExtra("Selected restaurant", result);
                itemView.getContext().startActivity(intent);
            });

//            binding.textViewDistance.setText(getDistance());

        }

        private String getDistance(String currentLocation, double destinationLat, double destinationLng) {
            DistanceCalculator calculator = new DistanceCalculator();
            int distance = Math.round(calculator.getDistance(currentLocation, destinationLat, destinationLng));
            return String.format("%sm", distance);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewItemBinding mListViewItemBinding = ListViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
}