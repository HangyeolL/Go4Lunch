package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import java.util.List;

public class ListViewState {

    private final List<ListItemViewState> listViewFragmentRecyclerViewItemViewStateList;

    public ListViewState(List<ListItemViewState> listViewFragmentRecyclerViewItemViewStateList) {
        this.listViewFragmentRecyclerViewItemViewStateList = listViewFragmentRecyclerViewItemViewStateList;
    }

    public List<ListItemViewState> getListViewFragmentRecyclerViewItemViewStateList() {
        return listViewFragmentRecyclerViewItemViewStateList;
    }

}
