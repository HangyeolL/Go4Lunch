package com.hangyeollee.go4lunch.ui.map;

import static com.hangyeollee.go4lunch.utils.UtilBox.makeDrawableIntoBitmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private MapViewModel viewModel;

    @NonNull
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapViewModel.class);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);

        viewModel.getMapsFragmentViewStateLiveData().observe(getViewLifecycleOwner(), mapsFragmentViewState -> {
            googleMap.clear();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapsFragmentViewState.getUserLatLng(), 14));

            for (MapMarkerViewState mapMarkerViewState : mapsFragmentViewState.getMapMarkerViewStateList()) {
                Marker marker = googleMap.addMarker(
                    new MarkerOptions()
                        .icon(getMapIcon(mapMarkerViewState))
                        .position(mapMarkerViewState.getPositionLatLng())
                        .title(mapMarkerViewState.getTitle())
                );
                marker.setTag(mapMarkerViewState.getPlaceId());
            }

            googleMap.setOnInfoWindowClickListener(
                marker -> viewModel.onMarkerClicked((String) marker.getTag())
            );
        });

        viewModel.getIntentSingleLiveEvent().observe(this,
            this::startActivity
        );
    }

    private BitmapDescriptor getMapIcon(MapMarkerViewState mapMarkerViewState) {
        Drawable markerIconDrawable = ResourcesCompat.getDrawable(getResources(), mapMarkerViewState.getIconRes(), null);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable).mutate();
        if (mapMarkerViewState.getTintRes() != null) {
            DrawableCompat.setTint(wrappedDrawable, requireContext().getColor(mapMarkerViewState.getTintRes()));
        }
        return makeDrawableIntoBitmap(wrappedDrawable);
    }

}