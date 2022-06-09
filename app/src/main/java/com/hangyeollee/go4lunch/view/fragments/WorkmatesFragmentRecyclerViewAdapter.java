package com.hangyeollee.go4lunch.view.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.WorkmatesListItemBinding;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;

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
        WorkmatesListItemBinding mWorkmatesListItemBinding = WorkmatesListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesFragmentRecyclerViewAdapter.ViewHolder(mWorkmatesListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(mUserList.get(position), mLunchRestaurantList);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WorkmatesListItemBinding binding;

        public ViewHolder(@NonNull WorkmatesListItemBinding workmatesListItemBinding) {
            super(workmatesListItemBinding.getRoot());
            binding = workmatesListItemBinding;
        }

        public void bindViews(User user, List<LunchRestaurant> lunchRestaurantList) {
            if (user.getPhotoUrl() != null) {
                Glide.with(itemView).load(user.getPhotoUrl()).into(binding.viewUserPhoto);
            }

            binding.textViewUserName.setText(user.getName());

            if(lunchRestaurantList.isEmpty()) {
                binding.textViewRestaruantName.setText("not decided yet");
            }

            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (lunchRestaurant.getUserId().equals(user.getId())) {
                    binding.textViewRestaruantName.setText(lunchRestaurant.getName());
                } else {
                    binding.textViewRestaruantName.setText("not decided yet");
                }
                break;
            }
        }
    }
}
//            itemView.setOnClickListener(listener -> {
//                Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
//                intent.putExtra("place id", result.getPlaceId());
//                itemView.getContext().startActivity(intent);
//            });



