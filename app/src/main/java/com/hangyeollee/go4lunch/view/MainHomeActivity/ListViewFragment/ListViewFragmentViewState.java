package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;

import java.util.List;

public class ListViewFragmentViewState {

    private final List<ListViewFragmentRecyclerViewItemViewState> listViewFragmentRecyclerViewItemViewStateList;
    private final boolean isProgressBarVisible;

    public ListViewFragmentViewState(List<ListViewFragmentRecyclerViewItemViewState> listViewFragmentRecyclerViewItemViewStateList, boolean isProgressBarVisible) {
        this.listViewFragmentRecyclerViewItemViewStateList = listViewFragmentRecyclerViewItemViewStateList;
        this.isProgressBarVisible = isProgressBarVisible;
    }

    public List<ListViewFragmentRecyclerViewItemViewState> getListViewFragmentRecyclerViewItemViewStateList() {
        return listViewFragmentRecyclerViewItemViewStateList;
    }

    public boolean isProgressBarVisible() {
        return isProgressBarVisible;
    }
}
