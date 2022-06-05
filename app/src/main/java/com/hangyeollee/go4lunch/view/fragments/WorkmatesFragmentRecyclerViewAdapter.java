package com.hangyeollee.go4lunch.view.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.WorkmatesListItemBinding;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;

import java.util.List;

public class WorkmatesFragmentRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesFragmentRecyclerViewAdapter.ViewHolder> {

    private List<User> mUserList;
    private LunchRestaurant mLunchRestaurant;

    public WorkmatesFragmentRecyclerViewAdapter(List<User> userList, LunchRestaurant lunchRestaurant) {
        mUserList = userList;
        mLunchRestaurant = lunchRestaurant;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesListItemBinding mWorkmatesListItemBinding = WorkmatesListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        WorkmatesListItemBinding binding;

        public ViewHolder(@NonNull WorkmatesListItemBinding workmatesListItemBinding) {
            super(workmatesListItemBinding.getRoot());
            binding = workmatesListItemBinding;
        }

        public void bindViews(User user) {
            if (user.getPhotoUrl() != null) {
                Glide.with(itemView).load(user.getPhotoUrl()).into(binding.viewUserPhoto);
            }

            binding.textViewUserName.setText(user.getName());

            if(mLunchRestaurant != null) {
                if(mLunchRestaurant.getName() != null) {
                    binding.textViewRestaruantName.setText(mLunchRestaurant.getName());
                } else {
                    binding.textViewRestaruantName.setText("not decided yet");

                }
            }

            //            itemView.setOnClickListener(listener -> {
            //                Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
            //                intent.putExtra("place id", result.getPlaceId());
            //                itemView.getContext().startActivity(intent);
            //            });
        }
    }
}
