package com.hangyeollee.go4lunch.view.MainHomeActivity.ListViewFragment;

import java.util.List;

public class ListViewFragmentViewState {

    private final List<ListViewFragmentRecyclerViewItemViewState> listViewFragmentRecyclerViewItemViewStateList;

    public ListViewFragmentViewState(List<ListViewFragmentRecyclerViewItemViewState> listViewFragmentRecyclerViewItemViewStateList) {
        this.listViewFragmentRecyclerViewItemViewStateList = listViewFragmentRecyclerViewItemViewStateList;
    }

    public List<ListViewFragmentRecyclerViewItemViewState> getListViewFragmentRecyclerViewItemViewStateList() {
        return listViewFragmentRecyclerViewItemViewStateList;
    }

}
