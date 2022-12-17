package com.hangyeollee.go4lunch.ui.workmates;

import java.util.List;
import java.util.Objects;

public class WorkmatesViewState {

    private final List<WorkmatesItemViewState> recyclerViewItemViewStateList;

    public WorkmatesViewState(List<WorkmatesItemViewState> recyclerViewItemViewStateList) {
        this.recyclerViewItemViewStateList = recyclerViewItemViewStateList;
    }

    public List<WorkmatesItemViewState> getRecyclerViewItemViewStateList() {
        return recyclerViewItemViewStateList;
    }

    @Override
    public String
    toString() {
        return "WorkmatesViewState{" +
            "recyclerViewItemViewStateList=" + recyclerViewItemViewStateList +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesViewState viewState = (WorkmatesViewState) o;
        return Objects.equals(recyclerViewItemViewStateList, viewState.recyclerViewItemViewStateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recyclerViewItemViewStateList);
    }
}
