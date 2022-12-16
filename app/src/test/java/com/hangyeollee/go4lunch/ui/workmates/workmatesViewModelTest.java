package com.hangyeollee.go4lunch.ui.workmates;

import static org.mockito.Mockito.doReturn;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.User;
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;
import com.hangyeollee.go4lunch.R;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class workmatesViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private FirebaseRepository firebaseRepository;
    private AutoCompleteDataRepository autoCompleteDataRepository;

    private final MutableLiveData<List<User>> userListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userInputMutableLiveData = new MutableLiveData<>();

    private WorkmatesViewModel viewModel;

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);
        autoCompleteDataRepository = Mockito.mock(AutoCompleteDataRepository.class);

        userListMutableLiveData.setValue(getDefaultUserList());
        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());
        userInputMutableLiveData.setValue("input");

        doReturn("Not going to restaurant (yet ?)").when(application).getString(R.string.not_going_to_restaurant_yet);

        doReturn(userListMutableLiveData).when(firebaseRepository).getUsersList();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();
        doReturn(userInputMutableLiveData).when(autoCompleteDataRepository).getUserInputMutableLiveData();

        viewModel = Mockito.mock(WorkmatesViewModel.class);
    }

//    @Test
//    public void nominal_case() {
//        // WHEN
//        WorkmatesViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesFragmentViewStateLiveData());
//
//        // THEN
//        WorkmatesViewState expectedViewState = new WorkmatesViewState(getDefaultWorkmatesItemViewStateList());
//
//        Assert.assertEquals(expectedViewState, viewState);
//    }

    //INPUTS
    private List<User> getDefaultUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(
                "userId1",
                "user1",
                "photoUrl1",
                null
            )
        );
        userList.add(new User(
                "userId2",
                "user2",
                "photoUrl2",
                null
            )
        );
        userList.add(new User(
                "userId3",
                "user3",
                "photoUrl3",
                null
            )
        );
        userList.add(new User(
                "userId4",
                "user4",
                "photoUrl4",
                null
            )
        );

        return userList;
    }

    private List<WorkmatesItemViewState> getDefaultWorkmatesItemViewStateList() {
        List<WorkmatesItemViewState> workmatesItemViewStateList = new ArrayList<>();
        workmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl1",
                "user1",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        workmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl2",
                "user2",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        workmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl3",
                "user3",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        workmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl4",
                "user4",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        return workmatesItemViewStateList;
    }
}
