package com.hangyeollee.go4lunch.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class RestaurantsListFragment extends Fragment {

    String location = "Paris";
    int radius = 1500;
    String type = "Bar";
    String key = "myKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
//        Call<MyNearBySearchData> call = MyRetrofitBuilder.getRetrofitBuilder().getRestaurantList(location, radius, type, key);
//        call.enqueue(new Callback<MyNearBySearchData>() {
//            @Override
//            public void onResponse(Call<MyNearBySearchData> call, Response<MyNearBySearchData> response) {
//                MyNearBySearchData myNearBySearchData = response.body();
//                List<Restaurant> mListRestau = myNearBySearchData.getResults();
//
//            }
//
//            @Override
//            public void onFailure(Call<MyNearBySearchData> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    


}