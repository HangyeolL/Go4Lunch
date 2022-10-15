package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragmentViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    private MediatorLiveData<WorkmatesFragmentViewState> workMatesFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public WorkmatesFragmentViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;

        LiveData<List<User>> userListLiveData = mFirebaseRepository.getUsersList();
        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = mFirebaseRepository.getLunchRestaurantListOfAllUsers();

        workMatesFragmentViewStateMediatorLiveData.addSource(userListLiveData, userList -> {
            combine(userList, lunchRestaurantListLiveData.getValue());
        });

        workMatesFragmentViewStateMediatorLiveData.addSource(lunchRestaurantListLiveData, lunchRestaurantList -> {
            combine(userListLiveData.getValue(), lunchRestaurantList);
        });

    }

    private void combine(List<User> userList, List<LunchRestaurant> lunchRestaurantList) {
        if (userList == null || lunchRestaurantList == null) {
            return;
        }

        List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        String userPhotoUrl = "";
        String lunchRestaurantName= "not decided yet";
        String lunchRestaurantId= "";

        for (User user: userList) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (lunchRestaurant.getUserId().equals(user.getId())) {

                    if(user.getPhotoUrl() != null) {
                        userPhotoUrl = user.getPhotoUrl();
                    }

                    if(lunchRestaurant != null) {
                        lunchRestaurantName = lunchRestaurant.getName();
                        lunchRestaurantId = lunchRestaurant.getRestaurantId();
                    }

                    WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                            new WorkmatesFragmentRecyclerViewItemViewState(
                                    userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                    recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                }
            }
        }

        if (recyclerViewItemViewStateList.isEmpty()) {
            for (User user : userList) {
                if(user.getPhotoUrl() != null) {
                    userPhotoUrl = user.getPhotoUrl();
                }
                WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                        new WorkmatesFragmentRecyclerViewItemViewState(userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                recyclerViewItemViewStateList.add(recyclerViewItemViewState);
            }
        }

        workMatesFragmentViewStateMediatorLiveData.setValue(new WorkmatesFragmentViewState(recyclerViewItemViewStateList));
    }

    public LiveData<WorkmatesFragmentViewState> getWorkmatesFragmentViewStateLiveData() {
        return workMatesFragmentViewStateMediatorLiveData;
    }
}
