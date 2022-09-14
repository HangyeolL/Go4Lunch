package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangyeollee.go4lunch.databinding.PlaceDetailActivityWorkmatesJoiningListItemBinding;
import com.hangyeollee.go4lunch.model.User;

import java.util.List;

public class PlaceDetailActivityWorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<PlaceDetailActivityWorkmatesRecyclerViewAdapter.ViewHolder> {

    private List<User> mUserList;

    public void submitList(List<User> userList) {
        mUserList = userList;
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
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PlaceDetailActivityWorkmatesJoiningListItemBinding binding;

        public ViewHolder(@NonNull PlaceDetailActivityWorkmatesJoiningListItemBinding placeDetailActivityWorkmatesJoiningListItemBinding) {
            super(placeDetailActivityWorkmatesJoiningListItemBinding.getRoot());
            binding = placeDetailActivityWorkmatesJoiningListItemBinding;
        }

        public void bindViews(User user) {
            Glide.with(itemView).load(user.getPhotoUrl()).into(binding.viewUserPhoto);
            binding.textViewUserName.setText(user.getName());

        }
    }

}


