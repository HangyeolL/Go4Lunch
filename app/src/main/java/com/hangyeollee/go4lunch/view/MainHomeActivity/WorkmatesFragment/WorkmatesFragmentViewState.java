package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import java.util.List;

public class WorkmatesFragmentViewState {

    private final List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList;

    public WorkmatesFragmentViewState(List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList) {
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
    }

    public List<WorkmatesFragmentRecyclerViewItemViewState> getRecyclerViewItemViewStateList() {
        return recyclerViewItemViewStateList;
    }

}
