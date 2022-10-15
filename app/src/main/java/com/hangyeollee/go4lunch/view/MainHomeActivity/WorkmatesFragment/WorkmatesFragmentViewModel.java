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

        for (User user: userList) {
            for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                if (lunchRestaurant.getUserId().equals(user.getId())) {

                    String userPhotoUrl = "";
                    String lunchRestaurantName= "";
                    String lunchRestaurantId= "";

                    if(user.getPhotoUrl() != null) {
                        userPhotoUrl = user.getPhotoUrl();
                    }

                    if(lunchRestaurant != null) {
                        lunchRestaurantName = lunchRestaurant.getName();
                        lunchRestaurantId = lunchRestaurant.getRestaurantId();
                    } else {
                        lunchRestaurantName = "not decided yet";
                    }

                    WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                            new WorkmatesFragmentRecyclerViewItemViewState(
                                    userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                    recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                    break;
                }
            }
        }

        workMatesFragmentViewStateMediatorLiveData.setValue(new WorkmatesFragmentViewState(recyclerViewItemViewStateList, false));
    }

    public LiveData<WorkmatesFragmentViewState> getWorkmatesFragmentViewStateLiveData() {
        return workMatesFragmentViewStateMediatorLiveData;
    }
}
