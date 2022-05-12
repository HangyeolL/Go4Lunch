package com.hangyeollee.go4lunch.view.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.GoogleMapsFragmentViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    private String mLocation;

    private GoogleMap mGoogleMap;
    private GoogleMapsFragmentViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(GoogleMapsFragmentViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_maps, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.startLocationRequest();

        mViewModel.getLocationLiveData().observe(getActivity(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mLocation = location.getLatitude() + "," +location.getLongitude();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //        LatLng userLocation = new LatLng()
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mGoogleMap.clear();
        mViewModel.getNearBySearchLiveData("48.402957725552795,2.699456359957351").observe(getViewLifecycleOwner(), new Observer<MyNearBySearchData>() {
            @Override
            public void onChanged(MyNearBySearchData myNearBySearchData) {
                for (Result mResult : myNearBySearchData.getResults()) {
                    LatLng restauLatLng = new LatLng(mResult.getGeometry().getLocation().getLat(), mResult.getGeometry().getLocation().getLng());

                    BitmapDescriptor markerIcon = setUpMapIcon();
                    mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(restauLatLng).title(mResult.getName()));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restauLatLng, 15));
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.stopLocationRequest();
    }

    private BitmapDescriptor setUpMapIcon() {
        Drawable markerIconDrawable = getResources().getDrawable(R.drawable.ic_baseline_local_dining_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable);
        DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.orange));
        BitmapDescriptor markerIcon = mViewModel.makeDrawableIntoBitmap(wrappedDrawable);
        return markerIcon;
    }


}