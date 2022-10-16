package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragmentViewModel extends ViewModel {

    private MediatorLiveData<WorkmatesFragmentViewState> workMatesFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public WorkmatesFragmentViewModel(FirebaseRepository firebaseRepository, AutoCompleteDataRepository autoCompleteDataRepository) {

        LiveData<List<User>> userListLiveData = firebaseRepository.getUsersList();
        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = firebaseRepository.getLunchRestaurantListOfAllUsers();
        LiveData<String> userInputLiveData = autoCompleteDataRepository.getUserInputMutableLiveData();

        workMatesFragmentViewStateMediatorLiveData.addSource(userListLiveData, userList -> {
            combine(userList, lunchRestaurantListLiveData.getValue(), userInputLiveData.getValue());
        });

        workMatesFragmentViewStateMediatorLiveData.addSource(lunchRestaurantListLiveData, lunchRestaurantList -> {
            combine(userListLiveData.getValue(), lunchRestaurantList, userInputLiveData.getValue());
        });

        workMatesFragmentViewStateMediatorLiveData.addSource(userInputLiveData, userInput ->
                combine(userListLiveData.getValue(), lunchRestaurantListLiveData.getValue(), userInput));

    }

    private void combine(List<User> userList, List<LunchRestaurant> lunchRestaurantList, String userInput) {
        if (userList == null || lunchRestaurantList == null) {
            return;
        }

        List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        String userPhotoUrl = "";
        String lunchRestaurantName = "not decided yet";
        String lunchRestaurantId = "";

        if (userInput == null) {
            if (lunchRestaurantList.isEmpty()) {
                for (User user : userList) {
                    if (user.getPhotoUrl() != null) {
                        userPhotoUrl = user.getPhotoUrl();
                    }
                    WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                            new WorkmatesFragmentRecyclerViewItemViewState(userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                    recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                }
            }
            // LunchResTauList is not Empty but userInput is null
            else {
                for (User user : userList) {
                    for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                        if (lunchRestaurant.getUserId().equals(user.getId())) {
                            if (lunchRestaurant != null) {
                                lunchRestaurantName = lunchRestaurant.getName();
                                lunchRestaurantId = lunchRestaurant.getRestaurantId();
                            }
                        }
                    }
                    if (user.getPhotoUrl() != null) {
                        userPhotoUrl = user.getPhotoUrl();
                    }

                    WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                            new WorkmatesFragmentRecyclerViewItemViewState(
                                    userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                    recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                }
            }
        }
        // UserInput is not null and not empty
        else if (!userInput.isEmpty()){
            if (lunchRestaurantList.isEmpty()) {
                for (User user : userList) {
                    // lunchRestauList is empty
                    // should sort out according to userInput
                    if (user.getName().contains(userInput)) {
                        if (user.getPhotoUrl() != null) {
                            userPhotoUrl = user.getPhotoUrl();
                        }
                        WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                                new WorkmatesFragmentRecyclerViewItemViewState(userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                        break;
                    }
                    // UserInput is not null, lunchRestaurantList is empty, userInput doesn't match to userName
                    // Should take all the users
                    else {
                        if (user.getPhotoUrl() != null) {
                            userPhotoUrl = user.getPhotoUrl();
                        }
                        WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                                new WorkmatesFragmentRecyclerViewItemViewState(userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                    }
                }
            }
            // UserInput is not null, lunchRestaurantList is not empty
            else {
                for (User user : userList) {
                    for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
                        if (lunchRestaurant.getUserId().equals(user.getId())) {
                            if (lunchRestaurant != null) {
                                lunchRestaurantName = lunchRestaurant.getName();
                                lunchRestaurantId = lunchRestaurant.getRestaurantId();
                            }
                        }
                    }
                    if (user.getName().contains(userInput)) {
                        if (user.getPhotoUrl() != null) {
                            userPhotoUrl = user.getPhotoUrl();
                        }
                        WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                                new WorkmatesFragmentRecyclerViewItemViewState(userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                        break;
                    }
                    else {
                        if (user.getPhotoUrl() != null) {
                            userPhotoUrl = user.getPhotoUrl();
                        }
                        WorkmatesFragmentRecyclerViewItemViewState recyclerViewItemViewState =
                                new WorkmatesFragmentRecyclerViewItemViewState(userPhotoUrl, user.getName(), lunchRestaurantName, lunchRestaurantId);

                        recyclerViewItemViewStateList.add(recyclerViewItemViewState);
                    }
                }
            }
        }
        // UserInput is not null but emtpy
        else if (userInput.isEmpty()){
            for(User user : userList) {
                if (user.getPhotoUrl() != null) {
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