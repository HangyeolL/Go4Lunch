package com.hangyeollee.go4lunch.view.MainHomeActivity.WorkmatesFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hangyeollee.go4lunch.model.LunchRestaurant;
import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;

import java.util.List;

public class WorkmatesFragmentViewModel extends ViewModel {

    private FirebaseRepository mFirebaseRepository;

    public WorkmatesFragmentViewModel(FirebaseRepository firebaseRepository) {
        mFirebaseRepository = firebaseRepository;
    }

    public LiveData<List<User>> getUsersList() {
        return mFirebaseRepository.getUsersList();
    }

    public LiveData<List<LunchRestaurant>> getLunchRestaurantListOfAllUsers() {
        return mFirebaseRepository.getLunchRestaurantListOfAllUsers();
    }
}
