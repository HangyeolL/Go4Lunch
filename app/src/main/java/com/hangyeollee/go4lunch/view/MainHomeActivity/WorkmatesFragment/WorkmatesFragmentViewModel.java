package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragmentViewModel extends ViewModel {

    private final Application context;

    private final MediatorLiveData<WorkmatesFragmentViewState> workMatesFragmentViewStateMediatorLiveData = new MediatorLiveData<>();

    public WorkmatesFragmentViewModel(
        Application context,
        FirebaseRepository firebaseRepository,
        AutoCompleteDataRepository autoCompleteDataRepository
    ) {
        this.context = context;
        LiveData<List<User>> userListLiveData = firebaseRepository.getUsersList();
        LiveData<List<LunchRestaurant>> lunchRestaurantListLiveData = firebaseRepository.getLunchRestaurantListOfAllUsers();
        LiveData<String> userInputLiveData = autoCompleteDataRepository.getUserInputMutableLiveData();

        workMatesFragmentViewStateMediatorLiveData.addSource(userListLiveData, userList ->
            combine(userList, lunchRestaurantListLiveData.getValue(), userInputLiveData.getValue())
        );

        workMatesFragmentViewStateMediatorLiveData.addSource(lunchRestaurantListLiveData, lunchRestaurantList ->
            combine(userListLiveData.getValue(), lunchRestaurantList, userInputLiveData.getValue())
        );

        workMatesFragmentViewStateMediatorLiveData.addSource(userInputLiveData, userInput ->
            combine(userListLiveData.getValue(), lunchRestaurantListLiveData.getValue(), userInput)
        );
    }

    private void combine(@Nullable List<User> userList, @Nullable List<LunchRestaurant> lunchRestaurantList, @Nullable String userInput) {
        if (userList == null || lunchRestaurantList == null) {
            return;
        }

        List<WorkmatesFragmentRecyclerViewItemViewState> recyclerViewItemViewStateList = new ArrayList<>();

        List<String> userIdsEatingToday = new ArrayList<>();

        for (LunchRestaurant lunchRestaurant : lunchRestaurantList) {
            User matchingUser = null;

            for (User user : userList) {
                if (user.getId().equalsIgnoreCase(lunchRestaurant.getUserId())) {
                    matchingUser = user;
                    break;
                }
            }

            if (matchingUser != null) {
                userIdsEatingToday.add(matchingUser.getId());

                if (userInput == null || matchingUser.getName().contains(userInput) || lunchRestaurant.getName().contains(userInput)) {
                    recyclerViewItemViewStateList.add(
                        new WorkmatesFragmentRecyclerViewItemViewState(
                            matchingUser.getPhotoUrl(),
                            matchingUser.getName(),
                            lunchRestaurant.getName(),
                            lunchRestaurant.getRestaurantId()
                        )
                    );
                }
            }
        }

        for (User user : userList) {
            if (!userIdsEatingToday.contains(user.getId())) {
                if (userInput == null || user.getName().contains(userInput)) {
                    recyclerViewItemViewStateList.add(
                        new WorkmatesFragmentRecyclerViewItemViewState(
                            user.getPhotoUrl(),
                            user.getName(),
                            context.getString(R.string.not_going_to_restaurant_yet),
                            null
                        )
                    );
                }
            }
        }

        workMatesFragmentViewStateMediatorLiveData.setValue(new WorkmatesFragmentViewState(recyclerViewItemViewStateList));
    }

    public LiveData<WorkmatesFragmentViewState> getWorkmatesFragmentViewStateLiveData() {
        return workMatesFragmentViewStateMediatorLiveData;
    }

}