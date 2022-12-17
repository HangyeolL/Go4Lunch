package com.hangyeollee.go4lunch.ui.place_detail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.model.LikedRestaurant;
import com.hangyeollee.go4lunch.data.model.LunchRestaurant;
import com.hangyeollee.go4lunch.data.model.User;
import com.hangyeollee.go4lunch.data.model.neaerbyserachpojo.OpeningHours;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.Geometry;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.MyPlaceDetailData;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.Photo;
import com.hangyeollee.go4lunch.data.model.placedetailpojo.Result;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.PlaceDetailDataRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PlaceDetailViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private FirebaseRepository firebaseRepository;
    private PlaceDetailDataRepository placeDetailDataRepository;

    private List<Photo> photoList;

    private final MutableLiveData<MyPlaceDetailData> myPlaceDetailDataMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LunchRestaurant>> lunchRestaurantListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LikedRestaurant>> likedRestaurantListMutableLiveData = new MutableLiveData<>();

    private final SingleLiveEvent<String> toastMessageSingleLiveEvent = new SingleLiveEvent<>();

    private PlaceDetailViewModel viewModel;

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        firebaseRepository = Mockito.mock(FirebaseRepository.class);
        placeDetailDataRepository = Mockito.mock(PlaceDetailDataRepository.class);

        photoList = new ArrayList<>();
        Photo photo = Mockito.mock(Photo.class);
        doReturn("photo").when(photo).getPhotoReference();
        photoList.add(photo);

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailData(
                new ArrayList<>(),
                new Result(
                    new Geometry(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHours(true),
                    photoList,
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

        doReturn(R.color.grey).when(application).getColor(R.color.grey);
        doReturn(R.color.yellow).when(application).getColor(R.color.yellow);

//        doReturn("website not available").when(application).getString(R.string.website_unavailable);

        viewModel = new PlaceDetailViewModel(application, placeDetailDataRepository, firebaseRepository);
    }

    @Test
    public void nominal_case_with_placeId1() {
        //WHEN
        viewModel.onPlaceIdFetched("placeId1");
        PlaceDetailViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getPlaceDetailActivityViewStateLiveData());

        //EXPECTED
        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photo&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
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
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photo&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
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
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photo&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
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
    public void edge_case_user1_selects_placeId1_as_liked_restaurant () {
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
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=photo&key=AIzaSyAe-yeU257vAO5EtWEyAO9Ofut-GsJjqeY",
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

    @Ignore
    @Test
    public void edge_case_website_unavailable_should_put_null_in_string_single_live_event() {
        // GIVEN
        viewModel.onPlaceIdFetched("placeId1");

        myPlaceDetailDataMutableLiveData.setValue(
            new MyPlaceDetailData(
                new ArrayList<>(),
                new Result(
                    new Geometry(),
                    "007",
                    "placeDetailDataResult",
                    new OpeningHours(true),
                    photoList,
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

        // THEN
        assertEquals("website not available", toastMessageSingleLiveEvent.getValue());
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
