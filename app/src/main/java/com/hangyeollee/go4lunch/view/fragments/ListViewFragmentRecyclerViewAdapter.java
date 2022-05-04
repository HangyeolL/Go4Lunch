package com.hangyeollee.go4lunch.view.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hangyeollee.go4lunch.databinding.ListViewItemBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;

import java.util.List;

public class ListViewFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ListViewFragmentRecyclerViewAdapter.myViewHolder> {

    private List<Result> mResultList;


    public ListViewFragmentRecyclerViewAdapter(List<Result> resultList) {
        mResultList = resultList;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ListViewItemBinding binding;

        public myViewHolder(@NonNull ListViewItemBinding mListViewItemBinding) {
            super(mListViewItemBinding.getRoot());
            binding = mListViewItemBinding;
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListViewItemBinding mListViewItemBinding = ListViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new myViewHolder(mListViewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Result mResult = mResultList.get(position);

        holder.binding.textViewName.setText(mResult.getName());
        holder.binding.textViewAddress.setText(mResult.getVicinity());

        // Was working not working anymore
//        if (mResult.getOpeningHours().getOpenNow()) {
//            holder.binding.textViewIsOpenNow.setText("OPEN");
//        } else {
//            holder.binding.textViewIsOpenNow.setText("CLOSED");
//        }

        if (mResult.getRating() <= 2.5) {
            holder.binding.imageViewStar3.setVisibility(View.VISIBLE);
        } else if (mResult.getRating() <= 4 && mResult.getRating() > 2.5) {
            holder.binding.imageViewStar3.setVisibility(View.VISIBLE);
            holder.binding.imageViewStar2.setVisibility(View.VISIBLE);
        } else if (mResult.getRating() > 4) {
            holder.binding.imageViewStar3.setVisibility(View.VISIBLE);
            holder.binding.imageViewStar2.setVisibility(View.VISIBLE);
            holder.binding.imageViewStar1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }
}