package com.hangyeollee.go4lunch.ui.place_detail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LikedRestaurant;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.User;
import com.hangyeollee.go4lunch.data.model.neaerbyserach.OpeningHoursResponse;
import com.hangyeollee.go4lunch.data.model.placedetail.GeometryResponse;
import com.hangyeollee.go4lunch.data.model.placedetail.MyPlaceDetailResponse;
import com.hangyeollee.go4lunch.data.model.placedetail.PhotoResponse;
import com.hangyeollee.go4lunch.data.model.placedetail.ResultResponse;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PlaceDetailViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private FirebaseRepository firebaseRepository;
    private PlaceDetailDataRepository placeDetailDataRepository;

    private List<PhotoResponse> photoResponseList;

    private final MutableLiveData<MyPlaceDetailResponse> myPlaceDetailDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LikedRestaurant>> likedRestaurantListMutableLiveData = new MutableLiveData<>();

    private PlaceDetailViewModel viewModel;

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);
        placeDetailDataRepository = Mockito.mock(PlaceDetailDataRepository.class);

        photoResponseList = new ArrayList<>();
        PhotoResponse photoResponse = Mockito.mock(PhotoResponse.class);
        doReturn("photoResponse").when(photoResponse).getPhotoReference();
        photoResponseList.add(photoResponse);

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    "website"
                ),
                "OK"
            )
        );
        userListMutableLiveData.setValue(getDefaultUserList());
        lunchRestaurantListMutableLiveData.setValue(new ArrayList<>());
        likedRestaurantListMutableLiveData.setValue(new ArrayList<>());

        doReturn(myPlaceDetailDataMutableLiveData).when(placeDetailDataRepository).getPlaceDetailLiveData("placeId1");
        doReturn(userListMutableLiveData).when(firebaseRepository).getUsersList();
        doReturn(lunchRestaurantListMutableLiveData).when(firebaseRepository).getLunchRestaurantListOfAllUsers();
        doReturn(likedRestaurantListMutableLiveData).when(firebaseRepository).getLikedRestaurantList();

        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
        doReturn(firebaseUser).when(firebaseRepository).getCurrentUser();
        doReturn("userId1").when(firebaseUser).getUid();

        doReturn(R.color.grey).when(application).getColor(R.color.grey);
        doReturn(R.color.yellow).when(application).getColor(R.color.yellow);
        doReturn("website not available").when(application).getString(R.string.website_unavailable);
        doReturn("international phone number not available").when(application).getString(R.string.international_phone_number_unavailable);
        doReturn("removed from the liked restaurant List").when(application).getString(R.string.removed_from_the_liked_restaurant_list);
        doReturn("added to liked restaurant list").when(application).getString(R.string.add_to_liked_restaurant_list);
        doReturn("you will go to ").when(application).getString(R.string.you_will_go_to);
        doReturn("you won\'t go to ").when(application).getString(R.string.you_will_not_go_to);
        doReturn(" for lunch").when(application).getString(R.string.for_lunch);

        viewModel = new PlaceDetailViewModel(application, placeDetailDataRepository, firebaseRepository);
    }

    @Test
    public void nominal_case_with_placeId1() {
        //WHEN
        viewModel.onPlaceIdFetched("placeId1");
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());

        //EXPECTED
        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
            "placeDetailDataResult",
            "Seoul",
            4.5f,
            "007",
            "website",
            new ArrayList<>(),
            R.color.grey,
            R.color.grey,
            false,
            false
        );

        //THEN
        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_user1_selects_placeId1_as_lunch_restaurant() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant(
                "placeId1",
                "userId1",
                "placeDetailDataResult",
                "2022-12-01"
            )
        );
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());

        // THEN
        List<PlaceDetailItemViewState> placeDetailItemViewStateList = new ArrayList<>();
        placeDetailItemViewStateList.add(new PlaceDetailItemViewState(
                "user1",
                "photoUrl1"
            )
        );
        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
            "placeDetailDataResult",
            "Seoul",
            4.5f,
            "007",
            "website",
            placeDetailItemViewStateList,
            R.color.grey,
            R.color.orange,
            false,
            true
        );

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_user1_and_user2_joining_at_the_same_restaurant_placeId1() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant(
                "placeId1",
                "userId1",
                "placeDetailDataResult",
                "2022-12-01"
            )
        );
        lunchRestaurantList.add(new LunchRestaurant(
                "placeId1",
                "userId2",
                "placeDetailDataResult",
                "2022-12-01"
            )
        );
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());

        // THEN
        List<PlaceDetailItemViewState> placeDetailItemViewStateList = new ArrayList<>();
        placeDetailItemViewStateList.add(new PlaceDetailItemViewState(
                "user1",
                "photoUrl1"
            )
        );
        placeDetailItemViewStateList.add(new PlaceDetailItemViewState(
                "user2",
                "photoUrl2"
            )
        );

        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
            "placeDetailDataResult",
            "Seoul",
            4.5f,
            "007",
            "website",
            placeDetailItemViewStateList,
            R.color.grey,
            R.color.orange,
            false,
            true
        );

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_user1_selects_placeId1_as_liked_restaurant() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        List<LikedRestaurant> likedRestaurantList = new ArrayList<>();
        likedRestaurantList.add(new LikedRestaurant(
                "placeId1",
                "placeDetailDataResult"
            )
        );
        likedRestaurantListMutableLiveData.setValue(likedRestaurantList);

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());

        // THEN
        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
            "placeDetailDataResult",
            "Seoul",
            4.5f,
            "007",
            "website",
            new ArrayList<>(),
            R.color.yellow,
            R.color.grey,
            true,
            false
        );

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void edge_case_rating_is_null_should_display_0 () {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    null,
                    "Seoul",
                    "website"
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());

        // THEN
        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photoResponse&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
            "placeDetailDataResult",
            "Seoul",
            0,
            "007",
            "website",
            new ArrayList<>(),
            R.color.grey,
            R.color.grey,
            false,
            false
        );

        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void onButtonCallClicked_international_phoneNumber_unavailable() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    null,
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    null
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());
        viewModel.onButtonCallClicked(viewState);

        String expectedString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());

        // THEN
        assertEquals("international phone number not available", expectedString);
    }

    @Test
    public void onButtonWebsiteClicked_website_unavailable() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    null
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());
        viewModel.onButtonWebsiteClicked(viewState);

        String expectedString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());

        // THEN
        assertEquals("website not available", expectedString);
    }

    @Test
    public void onButtonLikeClicked_not_selected_as_liked_restaurant() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    "website"
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());
        viewModel.onButtonLikeClicked(viewState);

        //THEN
        Mockito.verify(firebaseRepository).addOrRemoveLikedRestaurant(
            "placeId1",
            viewState.getName(),
            viewState.isSelectedAsLikedRestaurant()
        );

        String actualString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());
        assertEquals("added to liked restaurant list", actualString);
    }

    @Test
    public void onButtonLikeClicked_selected_as_liked_restaurant() {
        // GIVEN
        List<LikedRestaurant> likedRestaurantList = new ArrayList<>();
        likedRestaurantList.add(new LikedRestaurant(
                "placeId1",
                "placeDetailDataResult"
            )
        );
        likedRestaurantListMutableLiveData.setValue(likedRestaurantList);

        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    "website"
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());
        viewModel.onButtonLikeClicked(viewState);

        //THEN
        Mockito.verify(firebaseRepository).addOrRemoveLikedRestaurant(
            "placeId1",
            viewState.getName(),
            viewState.isSelectedAsLikedRestaurant()
        );

        String actualString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());
        assertEquals("removed from the liked restaurant List", actualString);
    }

    @Test
    public void onFloatingActionButtonClicked_not_selected_as_lunch_restaurant() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    "website"
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());
        viewModel.onFloatingActionButtonClicked(viewState);
        String actualString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());

        // THEN
        Mockito.verify(firebaseRepository).addOrRemoveLunchRestaurant(
            "placeId1",
            "userId1",
            "placeDetailDataResult",
            LocalDate.now().toString(),
            false
        );
        assertEquals(
            "you will go to "
                + "placeDetailDataResult"
                + " for lunch",
            actualString
        );
    }

    @Test
    public void onFloatingActionButtonClicked_selected_as_lunch_restaurant() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        List<LunchRestaurant> lunchRestaurantList = new ArrayList<>();
        lunchRestaurantList.add(new LunchRestaurant(
                "placeId1",
                "userId1",
                "placeDetailDataResult",
                LocalDate.now().toString()
            )
        );
        lunchRestaurantListMutableLiveData.setValue(lunchRestaurantList);

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailResponse(
                new ArrayList<>(),
                new ResultResponse(
                    new GeometryResponse(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHoursResponse(true),
                    photoResponseList,
                    4.5,
                    "Seoul",
                    "website"
                ),
                "OK"
            )
        );

        // WHEN
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());
        viewModel.onFloatingActionButtonClicked(viewState);
        String actualString = LiveDataTestUtils.getValueForTesting(viewModel.getToastMessageSingleLiveEvent());

        // THEN
        Mockito.verify(firebaseRepository).addOrRemoveLunchRestaurant(
            "placeId1",
            "userId1",
            "placeDetailDataResult",
            LocalDate.now().toString(),
            true
        );
        assertEquals(
            "you won\'t go to "
                + "placeDetailDataResult",
            actualString
        );
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


}
