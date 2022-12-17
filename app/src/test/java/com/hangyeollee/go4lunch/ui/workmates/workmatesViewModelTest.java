package com.hangyeollee.go4lunch.ui.workmates;

import static org.junit.Assert.assertEquals;
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
        userInputMutableLiveData.setValue(null);

        doReturn(userListMutableLiveData).when(firebaseRepository).getUsersList();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();
        doReturn(userInputMutableLiveData).when(autoCompleteDataRepository).getUserInputMutableLiveData();

        doReturn("Not going to restaurant (yet ?)").when(application).getString(R.string.not_going_to_restaurant_yet);

        viewModel = new WorkmatesViewModel(
            application,
            firebaseRepository,
            autoCompleteDataRepository
        );
    }

    @Test
    public void nominal_case() {
        // WHEN
        WorkmatesViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesFragmentViewStateLiveData());

        // THEN
        WorkmatesViewState expectedViewState = new WorkmatesViewState(getDefaultWorkmatesItemViewStateList());

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_userInput_is_user1() {
        //GIVEN
        userInputMutableLiveData.setValue("user1");

        //WHEN
        WorkmatesViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesFragmentViewStateLiveData());

        //THEN
        List<WorkmatesItemViewState> expectedWorkmatesItemViewStateList = new ArrayList<>();
        expectedWorkmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl1",
                "user1",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        WorkmatesViewState expectedViewState = new WorkmatesViewState(expectedWorkmatesItemViewStateList);

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_user1_and_user2_chose_lunch_restaurant_rest_of_users_did_not_yet() {
        //GIVEN
        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant("placeId1", "userId1", "happy food", "2022-12-01"));
        lunchRestaurantList.add(new LunchRestaurant("placeId2", "userId2", "happy food2", "2022-12-01"));
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        //WHEN
        WorkmatesViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesFragmentViewStateLiveData());

        //THEN
        List<WorkmatesItemViewState> expectedWorkmatesItemViewStateList = new ArrayList<>();
        expectedWorkmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl1",
                "user1",
                "happy food",
                "placeId1"
            )
        );
        expectedWorkmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl2",
                "user2",
                "happy food2",
                "placeId2"
            )
        );
        expectedWorkmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl3",
                "user3",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        expectedWorkmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl4",
                "user4",
                application.getString(R.string.not_going_to_restaurant_yet),
                null
            )
        );
        WorkmatesViewState expectedViewState = new WorkmatesViewState(expectedWorkmatesItemViewStateList);

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_userInput_is_happy_food3_and_user3_selected_happy_food3_as_lunch_restaurant() {
        //GIVEN
        userInputMutableLiveData.setValue("happy food3");

        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant("placeId3", "userId3", "happy food3", "2022-12-01"));
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        //WHEN
        WorkmatesViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getWorkmatesFragmentViewStateLiveData());

        //THEN
        List<WorkmatesItemViewState> expectedWorkmatesItemViewStateList = new ArrayList<>();
        expectedWorkmatesItemViewStateList.add(new WorkmatesItemViewState(
                "photoUrl3",
                "user3",
                "happy food3",
                "placeId3"
            )
        );
        WorkmatesViewState expectedViewState = new WorkmatesViewState(expectedWorkmatesItemViewStateList);

        assertEquals(expectedViewState, viewState);
    }

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
