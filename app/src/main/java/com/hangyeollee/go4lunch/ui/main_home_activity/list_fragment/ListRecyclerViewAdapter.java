package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.protobuf.StringValue;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ListViewFragmentListItemBinding;
import com.hangyeollee.go4lunch.ui.place_detail_activity.PlaceDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private List<ListItemViewState> listViewFragmentRecyclerViewItemViewStateList = new ArrayList<>();

    public void submitList(List<ListItemViewState> itemViewStateList) {
        listViewFragmentRecyclerViewItemViewStateList = itemViewStateList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewFragmentListItemBinding binding = ListViewFragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViews(listViewFragmentRecyclerViewItemViewStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return listViewFragmentRecyclerViewItemViewStateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListViewFragmentListItemBinding binding;

        public ViewHolder(@NonNull ListViewFragmentListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindViews(ListItemViewState itemViewState) {
            binding.textViewName.setText(itemViewState.getName());
            binding.textViewAddress.setText(itemViewState.getVicinity());
            binding.textViewDistance.setText(itemViewState.getDistanceFromUserLocation());
            binding.ratingBar.setRating(itemViewState.getRating());
            binding.textViewIsOpenNow.setText(itemViewState.getIsOpenText());
            binding.textViewIsOpenNow.setTextColor(itemViewState.getIsOpenTextColor());
            binding.textViewWorkmatesNumber.setText(String.valueOf(itemViewState.getWorkmatesJoiningNumber()));

            Glide.with(itemView)
                    .load(itemViewState.getPhotoReference())
                    .into(binding.imageViewRestaurant);

            itemView.setOnClickListener(i ->
                    itemView.getContext().startActivity(
                            PlaceDetailActivity.navigate(
                                    itemView.getContext(),
                                    itemViewState.getPlaceId()
                            )
                    )
            );


        }

    }
}