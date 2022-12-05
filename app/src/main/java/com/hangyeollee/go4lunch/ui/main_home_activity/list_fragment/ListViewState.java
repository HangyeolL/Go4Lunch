package com.hangyeollee.go4lunch.ui.main_home_activity.list_fragment;

import java.util.List;
import java.util.Objects;

public class ListViewState {

    private final List<ListItemViewState> listViewFragmentRecyclerViewItemViewStateList;

    public ListViewState(List<ListItemViewState> listViewFragmentRecyclerViewItemViewStateList) {
        this.listViewFragmentRecyclerViewItemViewStateList = listViewFragmentRecyclerViewItemViewStateList;
    }

    public List<ListItemViewState> getListViewFragmentRecyclerViewItemViewStateList() {
        return listViewFragmentRecyclerViewItemViewStateList;
    }

    @Override
    public String toString() {
        return "ListViewState{" +
                "listViewFragmentRecyclerViewItemViewStateList=" + listViewFragmentRecyclerViewItemViewStateList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListViewState that = (ListViewState) o;
        return Objects.equals(listViewFragmentRecyclerViewItemViewStateList, that.listViewFragmentRecyclerViewItemViewStateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listViewFragmentRecyclerViewItemViewStateList);
    }
}
