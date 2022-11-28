package com.hangyeollee.go4lunch.ui.main_home_activity.workmates_fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.WorkmatesFragmentListItemBinding;
import com.hangyeollee.go4lunch.ui.place_detail_activity.PlaceDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesRecyclerViewAdapter.ViewHolder> {

    private List<WorkmatesItemViewState> workmatesItemViewStateList = new ArrayList<>();

    public void submitList(List<WorkmatesItemViewState> workmatesItemViewStateList) {
        this.workmatesItemViewStateList = workmatesItemViewStateList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesFragmentListItemBinding mWorkmatesListItemBinding = WorkmatesFragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(mWorkmatesListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(workmatesItemViewStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return workmatesItemViewStateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WorkmatesFragmentListItemBinding binding;

        public ViewHolder(@NonNull WorkmatesFragmentListItemBinding workmatesListItemBinding) {
            super(workmatesListItemBinding.getRoot());
            binding = workmatesListItemBinding;
        }

        public void bindViews(WorkmatesItemViewState itemViewState) {
            Glide.with(itemView).load(itemViewState.getUserPhotoUrl()).into(binding.viewUserPhoto);
            binding.textViewUserName.setText(itemViewState.getUserName());
            binding.textViewRestaurantName.setText(itemViewState.getUserLunchRestaurantName());

            itemView.setOnClickListener(listener -> {
                itemView.getContext()
                        .startActivity(
                                PlaceDetailActivity.navigate(itemView.getContext(),
                                        itemViewState.getUserLunchRestaurantId()
                                )
                        );
            });

        }

    }
}





