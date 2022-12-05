package com.hangyeollee.go4lunch.ui.workmates;

import java.util.List;

public class WorkmatesViewState {

    private final List<WorkmatesItemViewState> recyclerViewItemViewStateList;

    public WorkmatesViewState(List<WorkmatesItemViewState> recyclerViewItemViewStateList) {
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
    }

    public List<WorkmatesItemViewState> getRecyclerViewItemViewStateList() {
        return recyclerViewItemViewStateList;
    }

}
