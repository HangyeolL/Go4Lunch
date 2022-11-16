package com.hangyeollee.go4lunch.ui.place_detail_activity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.PlaceDetailActivityWorkmatesJoiningListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailRecyclerViewAdapter extends RecyclerView.Adapter<PlaceDetailRecyclerViewAdapter.ViewHolder> {

    private List<PlaceDetailItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

    public void submitList(List<PlaceDetailItemViewState> recyclerViewItemViewStateList) {
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceDetailActivityWorkmatesJoiningListItemBinding binding = PlaceDetailActivityWorkmatesJoiningListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(recyclerViewItemViewStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return recyclerViewItemViewStateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PlaceDetailActivityWorkmatesJoiningListItemBinding binding;

        public ViewHolder(@NonNull PlaceDetailActivityWorkmatesJoiningListItemBinding placeDetailActivityWorkmatesJoiningListItemBinding) {
            super(placeDetailActivityWorkmatesJoiningListItemBinding.getRoot());
            binding = placeDetailActivityWorkmatesJoiningListItemBinding;
        }

        public void bindView(PlaceDetailItemViewState recyclerViewItemViewState) {
            Glide.with(itemView).load(recyclerViewItemViewState.getUserPhotoUrl()).into(binding.viewUserPhoto);
            binding.textViewUserName.setText(recyclerViewItemViewState.getUserName());
        }
    }

}


