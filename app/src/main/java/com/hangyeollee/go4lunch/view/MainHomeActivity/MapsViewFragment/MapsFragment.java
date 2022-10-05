package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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

public class MapsFragment extends SupportMapFragment {

    private MapsFragmentViewModel mViewModel;

    private GoogleMap mGoogleMap;

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapsFragmentViewModel.class);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "Is already granted");
            onMapReady();
        } else {
            Log.d("Permission", "is not granted launch permission dialog");
        }

    }


    private void onMapReady() {
        getMapAsync(new OnMapReadyCallback() {
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

                            if(mapsFragmentViewState.isProgressBarVisible()) {
                                getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                            } else {
                                getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                            }

                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapsFragmentViewState.getUserLatLng(), 15));

                            addMarkersOnMap(mapsFragmentViewState.getMyNearBySearchDataResultList());
                            setListenerOnMarker(mapsFragmentViewState.getMyNearBySearchDataResultList());
                        }
                    });

                }
            });
    }

    private BitmapDescriptor setUpMapIcon() {
        Drawable markerIconDrawable = getResources().getDrawable(R.drawable.ic_baseline_local_dining_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable);
        DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.orange));
        return mViewModel.makeDrawableIntoBitmap(wrappedDrawable);
    }

    private void addMarkersOnMap(List<Result> nearBySearchResultList) {
        BitmapDescriptor markerIcon = setUpMapIcon();

        for (Result mResult : nearBySearchResultList) {
            LatLng restauLatLng = new LatLng(mResult.getGeometry().getLocation().getLat(), mResult.getGeometry().getLocation().getLng());
            mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(restauLatLng).title(mResult.getName()));
        }
    }



    private void setListenerOnMarker(List<Result> nearBySearchResultList) {
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Log.i("markerName", marker.getTitle());

                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                for (Result result : nearBySearchResultList) {
                    if (marker.getTitle().equalsIgnoreCase(result.getName())) {
                        Log.e("sendingPlaceId", result.getPlaceId());
                        intent.putExtra("place id", result.getPlaceId());
                    }
                }
                startActivity(intent);
            }
        });
    }

//    private void autoCompleteMarkersOnMap() {
//        mGoogleMap.clear();
//        BitmapDescriptor markerIcon = setUpMapIcon();
//
//        mViewModel.getAutoCompleteLiveData().observe(getViewLifecycleOwner(), new Observer<MyAutoCompleteData>() {
//            @Override
//            public void onChanged(MyAutoCompleteData myAutoCompleteData) {
//                if (myAutoCompleteData != null) {
//                    mPredictionList = myAutoCompleteData.getPredictions();
//
//                    for (Result result : nearBySearchResultList) {
//                        for (Prediction prediction : mPredictionList) {
//                            if (prediction.getStructuredFormatting().getMainText().contains(result.getName())) {
//                                LatLng restauLatLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
//                                mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(restauLatLng).title(result.getName()));
//                            }
//                        }
//                    }
//                }
//
//            }
//        });
//    }



}