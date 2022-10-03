package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import java.util.List;

public class WorkmatesFragmentViewState {

    private final List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList;
    private final boolean isProgressBarVisible;

    public WorkmatesFragmentViewState(List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList, boolean isProgressBarVisible) {
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
        this.isProgressBarVisible = isProgressBarVisible;
    }

    public List<WorkmatesFragmentRecyclerViewItemViewState> getRecyclerViewItemViewStateList() {
        return recyclerViewItemViewStateList;
    }

    public boolean isProgressBarVisible() {
        return isProgressBarVisible;
    }
}
