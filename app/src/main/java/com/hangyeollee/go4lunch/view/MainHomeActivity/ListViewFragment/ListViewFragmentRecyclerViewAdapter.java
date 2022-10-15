package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.databinding.ListViewFragmentListItemBinding;
import com.hangyeollee.go4lunch.view.PlaceDetailActivity.PlaceDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ListViewFragmentRecyclerViewAdapter.ViewHolder> {

    private List<ListViewFragmentRecyclerViewItemViewState> listViewFragmentRecyclerViewItemViewStateList = new ArrayList<>();

    public void submitList(List<ListViewFragmentRecyclerViewItemViewState> itemViewStateList) {
        listViewFragmentRecyclerViewItemViewStateList = itemViewStateList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewFragmentListItemBinding mListViewItemBinding = ListViewFragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(mListViewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(listViewFragmentRecyclerViewItemViewStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return listViewFragmentRecyclerViewItemViewStateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListViewFragmentListItemBinding binding;

        public ViewHolder(@NonNull ListViewFragmentListItemBinding mListViewItemBinding) {
            super(mListViewItemBinding.getRoot());
            binding = mListViewItemBinding;
        }

        public void bindViews(ListViewFragmentRecyclerViewItemViewState itemViewState) {
            binding.textViewName.setText(itemViewState.getName());
            binding.textViewAddress.setText(itemViewState.getVicinity());

            if (itemViewState.isOpen()) {
                binding.textViewIsOpenNow.setText("OPEN");
                binding.textViewIsOpenNow.setTextColor(Color.BLUE);
            } else {
                binding.textViewIsOpenNow.setText("CLOSED");
                binding.textViewIsOpenNow.setTextColor(Color.RED);
            }

            binding.ratingBar.setRating(itemViewState.getRating());
            Glide.with(itemView).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + itemViewState.getPhotoReference() + "&key=" + BuildConfig.PLACES_API_KEY).into(binding.imageViewRestaurant);

            itemView.setOnClickListener(i -> {
                Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
                intent.putExtra("place id", itemViewState.getPlaceId());
                itemView.getContext().startActivity(intent);
            });

            binding.textViewDistance.setText(itemViewState.getDistanceFromUserLocation());
        }

    }
}