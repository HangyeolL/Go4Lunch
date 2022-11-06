package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private MapsFragmentViewModel viewModel;

    @NonNull
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapsFragmentViewModel.class);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);

        viewModel.getMapsFragmentViewStateLiveData().observe(getViewLifecycleOwner(),
                mapsFragmentViewState -> {
                    googleMap.clear();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapsFragmentViewState.getUserLatLng(), 14));

                    viewModel.onMapReady(mapsFragmentViewState.getMapMarkerViewStateList(), googleMap);

                    googleMap.setOnInfoWindowClickListener(
                            marker -> viewModel.onMarkerClicked(mapsFragmentViewState.getMapMarkerViewStateList(), marker)
                    );
                }
        );

        viewModel.getIntentSingleLiveEvent().observe(this,
                intent -> {
                    startActivity(intent);
                }
        );
    }

}