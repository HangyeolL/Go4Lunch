package com.hangyeollee.go4lunch.view.activities;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.PlaceDetailActivityWorkmatesJoiningListItemBinding;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivityWorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<PlaceDetailActivityWorkmatesRecyclerViewAdapter.ViewHolder> {

    private List<User> mUserList = new ArrayList<>();
    private List<LunchRestaurant> mLunchRestaurantList = new ArrayList<>();
    private Result mResult;

    private int mListSize;

    public void setUserList(List<User> userList) {
        mUserList = userList;
    }

    public void setLunchRestaurantList(List<LunchRestaurant> lunchRestaurantList) {
        mLunchRestaurantList = lunchRestaurantList;
    }

    public void setPlaceDetailResult(Result result) {
        mResult = result;
    }

    public void setListSize(int listSize) {
        mListSize = listSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceDetailActivityWorkmatesJoiningListItemBinding binding = PlaceDetailActivityWorkmatesJoiningListItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaceDetailActivityWorkmatesRecyclerViewAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mListSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PlaceDetailActivityWorkmatesJoiningListItemBinding binding;

        public ViewHolder(@NonNull PlaceDetailActivityWorkmatesJoiningListItemBinding placeDetailActivityWorkmatesJoiningListItemBinding) {
            super(placeDetailActivityWorkmatesJoiningListItemBinding.getRoot());
            binding = placeDetailActivityWorkmatesJoiningListItemBinding;
        }

        public void bindViews(User user) {
            for (LunchRestaurant lunchRestaurant : mLunchRestaurantList) {
                if (user.getId().equals(lunchRestaurant.getUserId()) && lunchRestaurant.getName().equals(mResult.getName())) {

                    binding.textViewUserName.setText(user.getName());
                    if (user.getPhotoUrl() != null) {
                        Glide.with(itemView).load(user.getPhotoUrl()).into(binding.viewUserPhoto);
                    }

                }
            }

        }
    }

}


