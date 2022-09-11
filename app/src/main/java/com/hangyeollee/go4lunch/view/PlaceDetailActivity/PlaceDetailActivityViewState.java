package com.hangyeollee.go4lunch.view.PlaceDetailActivity;

import com.hangyeollee.go4lunch.model.User;
import com.hangyeollee.go4lunch.model.placedetailpojo.Result;

import java.util.List;

public class PlaceDetailActivityViewState {
    private List<User> userList;
    private Boolean isSelectedAsLikedRestaurant;
    private Boolean isSelectedAsLunchRestaurant;
    private Result result;

    public PlaceDetailActivityViewState(List<User> userList, Boolean isSelectedAsLikedRestaurant, Boolean isSelectedAsLunchRestaurant, Result result) {
        this.userList = userList;
        this.isSelectedAsLikedRestaurant = isSelectedAsLikedRestaurant;
        this.isSelectedAsLunchRestaurant = isSelectedAsLunchRestaurant;
        this.result = result;
    }

    public List<User> getUserList() {
        return userList;
    }

    public Boolean getSelectedAsLikedRestaurant() {
        return isSelectedAsLikedRestaurant;
    }

    public Boolean getSelectedAsLunchRestaurant() {
        return isSelectedAsLunchRestaurant;
    }

    public Result getResult() {
        return result;
    }
}
