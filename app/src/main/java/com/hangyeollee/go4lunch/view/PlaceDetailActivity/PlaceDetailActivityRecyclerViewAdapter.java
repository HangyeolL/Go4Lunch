package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.PlaceDetailActivityWorkmatesJoiningListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivityRecyclerViewAdapter extends RecyclerView.Adapter<PlaceDetailActivityRecyclerViewAdapter.ViewHolder> {

    private List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

    public void submitList(List<PlaceDetailActivityRecyclerViewItemViewState> recyclerViewItemViewStateList) {
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

        public void bindView(PlaceDetailActivityRecyclerViewItemViewState recyclerViewItemViewState) {
            Glide.with(itemView).load(recyclerViewItemViewState.getUserPhotoUrl()).into(binding.viewUserPhoto);
            binding.textViewUserName.setText(recyclerViewItemViewState.getUserName());
        }
    }

}


