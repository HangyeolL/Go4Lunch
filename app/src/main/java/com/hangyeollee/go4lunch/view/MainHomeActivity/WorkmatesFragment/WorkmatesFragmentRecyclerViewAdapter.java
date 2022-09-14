package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.WorkmatesFragmentListItemBinding;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.view.PlaceDetailActivity.PlaceDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragmentRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesFragmentRecyclerViewAdapter.ViewHolder> {

    private List<User> mUserList = new ArrayList<>();
    private List<LunchRestaurant> mLunchRestaurantList = new ArrayList<>();

    public WorkmatesFragmentRecyclerViewAdapter() {
    }

    public void updateUserLists(List<User> userList) {
        mUserList = userList;
        notifyDataSetChanged();
    }

    public void updateRestaurantList(List<LunchRestaurant> lunchRestaurantList) {
        mLunchRestaurantList = lunchRestaurantList;
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
        holder.bindViews(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WorkmatesFragmentListItemBinding binding;

        public ViewHolder(@NonNull WorkmatesFragmentListItemBinding workmatesListItemBinding) {
            super(workmatesListItemBinding.getRoot());
            binding = workmatesListItemBinding;
        }

        public void bindViews(User user) {
            if (user.getPhotoUrl() != null) {
                Glide.with(itemView).load(user.getPhotoUrl()).into(binding.viewUserPhoto);
            }

            binding.textViewUserName.setText(user.getName());

            LunchRestaurant usersLunch = null;

            for (LunchRestaurant lunchRestaurant : mLunchRestaurantList) {
                if (lunchRestaurant.getUserId().equals(user.getId())) {
                    usersLunch = lunchRestaurant;
                    break;
                }
            }
            
            if(usersLunch != null) {
                binding.textViewRestaruantName.setText(usersLunch.getName());

                LunchRestaurant finalUsersLunch = usersLunch;
                itemView.setOnClickListener(listener -> {
                    Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
                    intent.putExtra("place id", finalUsersLunch.getRestaurantId());
                    itemView.getContext().startActivity(intent);
                });
            } else {
                binding.textViewRestaruantName.setText("not decided yet");
                binding.textViewUserName.setTextColor(Color.GRAY);
            }
        }
    }
}




