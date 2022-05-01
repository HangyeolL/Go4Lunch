package com.hangyeollee.go4lunch.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hangyeollee.go4lunch.BuildConfig;
import com.hangyeollee.go4lunch.api.NearbySearchApi;
import com.hangyeollee.go4lunch.databinding.FragmentListViewBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.repository.NearBySearchRepository;
import com.hangyeollee.go4lunch.utility.MyRetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListViewFragment extends Fragment implements LocationListener {

    private FragmentListViewBinding binding;

    NearbySearchApi mNearbySearchApi;
    String mLocation;

    double latitude = 48.8596639461606;
    double longitude = 2.2954959212058954;
    int radius = 1500;
    String type = "restaurant";
    String key = BuildConfig.MAPS_API_KEY;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);

//        mNearbySearchApi = MyRetrofitBuilder.getRetrofit().create(NearbySearchApi.class);
//
//        Call<MyNearBySearchData> call = mNearbySearchApi.getNearbySearchData(mLocation,radius, type, key);
//        call.enqueue(new Callback<MyNearBySearchData>() {
//            @Override
//            public void onResponse(Call<MyNearBySearchData> call, Response<MyNearBySearchData> response) {
//                Log.d("Retrofit", call.request().toString());
//                MyNearBySearchData myNearBySearchData = response.body();
//                Log.d("ListViewFragment", response.body().getResults().get(0).getName() + "");
//                List<Result> resultList =  myNearBySearchData.getResults();
//                binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(resultList, getActivity()));
//            }
//
//            @Override
//            public void onFailure(Call<MyNearBySearchData> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        mNearbySearchApi = MyRetrofitBuilder.getNearBySearchApi();
        NearBySearchRepository mNearbySearchRepo = new NearBySearchRepository(mLocation, mNearbySearchApi);
        Call<MyNearBySearchData> call = mNearbySearchRepo.getNearbySearchData();
        call.enqueue(new Callback<MyNearBySearchData>() {
            @Override
            public void onResponse(Call<MyNearBySearchData> call, Response<MyNearBySearchData> response) {
                Log.d("Retrofit", call.request().toString());
                List<Result> resultList = response.body().getResults();
                binding.recyclerViewRestaurantList.setAdapter(new ListViewFragmentRecyclerViewAdapter(resultList));
            }

            @Override
            public void onFailure(Call<MyNearBySearchData> call, Throwable t) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLocation = location.getLatitude() + "," + location.getLongitude();
        Log.d("My location is", location.toString());
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}