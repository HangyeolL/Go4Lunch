package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.view.PlaceDetailActivity.PlaceDetailActivity;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

import java.util.List;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private MapsFragmentViewModel mViewModel;
    private GoogleMap mGoogleMap;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapsFragmentViewModel.class);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mGoogleMap.clear();
        mGoogleMap.setMyLocationEnabled(true);

        mViewModel.getMapsFragmentViewStateLiveData().observe(getViewLifecycleOwner(), new Observer<MapsFragmentViewState>() {
            @Override
            public void onChanged(MapsFragmentViewState mapsFragmentViewState) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapsFragmentViewState.getUserLatLng(), 15));
                addMarkersOnMap(mapsFragmentViewState.getMapMarkerViewStateList());
                setListenerOnMarker(mapsFragmentViewState.getMapMarkerViewStateList());
            }
        });
    }

    private BitmapDescriptor setUpMapIcon() {
        Drawable markerIconDrawable = getResources().getDrawable(R.drawable.ic_baseline_local_dining_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable);
        DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.orange));
        return mViewModel.makeDrawableIntoBitmap(wrappedDrawable);
    }

    private void addMarkersOnMap(List<MapMarkerViewState> mapMarkerViewStateList) {
        BitmapDescriptor markerIcon = setUpMapIcon();

        for (MapMarkerViewState mapMarkerViewState : mapMarkerViewStateList) {
            mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(mapMarkerViewState.getPositionLatLng()).title(mapMarkerViewState.getTitle()));
        }
    }

    private void setListenerOnMarker(List<MapMarkerViewState> mapMarkerViewStateList) {
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Log.i("markerName", marker.getTitle());
                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                for (MapMarkerViewState mapMarkerViewState : mapMarkerViewStateList) {
                    Log.e("sendingPlaceId", mapMarkerViewState.getPlaceId());
                    intent.putExtra("place id", mapMarkerViewState.getPlaceId());
                }
                startActivity(intent);
            }
        });
    }
}