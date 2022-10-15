package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.WorkmatesFragmentListItemBinding;
import com.hangyeollee.go4lunch.view.PlaceDetailActivity.PlaceDetailActivity;

import java.util.List;

public class WorkmatesFragmentRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesFragmentRecyclerViewAdapter.ViewHolder> {

    private List<WorkmatesFragmentRecyclerViewItemViewState> workmatesFragmentRecyclerViewItemViewStateList;

    public void submitList(List<WorkmatesFragmentRecyclerViewItemViewState> workmatesFragmentRecyclerViewItemViewStateList) {
        this.workmatesFragmentRecyclerViewItemViewStateList = workmatesFragmentRecyclerViewItemViewStateList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesFragmentListItemBinding mWorkmatesListItemBinding = WorkmatesFragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesFragmentRecyclerViewAdapter.ViewHolder(mWorkmatesListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(workmatesFragmentRecyclerViewItemViewStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return workmatesFragmentRecyclerViewItemViewStateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WorkmatesFragmentListItemBinding binding;

        public ViewHolder(@NonNull WorkmatesFragmentListItemBinding workmatesListItemBinding) {
            super(workmatesListItemBinding.getRoot());
            binding = workmatesListItemBinding;
        }

        public void bindViews(WorkmatesFragmentRecyclerViewItemViewState itemViewState) {
            Glide.with(itemView).load(itemViewState.getUserPhotoUrl()).into(binding.viewUserPhoto);
            binding.textViewUserName.setText(itemViewState.getUserName());
            binding.textViewRestaruantName.setText(itemViewState.getUserLunchRestaurantName());

            itemView.setOnClickListener(listener -> {
                Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
                intent.putExtra("place id", itemViewState.getGetUserLunchRestaurantId());
                itemView.getContext().startActivity(intent);
            });

//            How to change textColor if lunchRestau is null ?
//            binding.textViewUserName.setTextColor(Color.GRAY);
        }
    }
}





