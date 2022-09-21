package com.hangyeollee.go4lunch.view.MainHomeActivity.MapsViewFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.LiveData;
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
import com.hangyeollee.go4lunch.model.autocompletepojo.MyAutoCompleteData;
import com.hangyeollee.go4lunch.model.autocompletepojo.Prediction;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.view.MainHomeActivity.MapsAndListSharedViewModel;
import com.hangyeollee.go4lunch.view.PlaceDetailActivity.PlaceDetailActivity;
import com.hangyeollee.go4lunch.view.ViewModelFactory;

import java.util.List;

public class MapsFragment extends SupportMapFragment {

    private String mUserLocation;
    private List<Result> nearBySearchResultList;
    private List<Prediction> mPredictionList;

    private boolean firstTime = false;

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

                    BitmapDescriptor markerIcon = setUpMapIcon();

                    mViewModel.getMapsFragmentViewStateMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<MapsFragmentViewState>() {
                        @Override
                        public void onChanged(MapsFragmentViewState mapsFragmentViewState) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapsFragmentViewState.getUserLatLng(), 15));

                            for (Result mResult : mapsFragmentViewState.getMyNearBySearchDataResultList()) {
                                LatLng restauLatLng = new LatLng(mResult.getGeometry().getLocation().getLat(), mResult.getGeometry().getLocation().getLng());
                                mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(restauLatLng).title(mResult.getName()));
                            }
                        }
                    });
//
//
//                    setListenerOnMarker();
//
//                    getLiveLocationAndFetchNearbySearchData();
//
//                    nearbySearchMarkersOnMap();
//
//                    searchViewSetup();

                }
            });
    }

    private void setListenerOnMarker() {
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

    private void getLiveLocationAndFetchNearbySearchData() {
        mViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mUserLocation = location.getLatitude() + "," + location.getLongitude();
//                mViewModel.fetchNearBySearchData(mUserLocation);

                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
            }
        });
    }

    private void nearbySearchMarkersOnMap() {
        mGoogleMap.clear();

//        mViewModel.getNearBySearchLiveData().observe(getViewLifecycleOwner(), new Observer<MyNearBySearchData>() {
//            @Override
//            public void onChanged(MyNearBySearchData myNearBySearchData) {
//                nearBySearchResultList = myNearBySearchData.getResults();
//
//                BitmapDescriptor markerIcon = setUpMapIcon();
//                for (Result mResult : nearBySearchResultList) {
//                    LatLng restauLatLng = new LatLng(mResult.getGeometry().getLocation().getLat(), mResult.getGeometry().getLocation().getLng());
//                    mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(restauLatLng).title(mResult.getName()));
//                }
//            }
//        });
    }

    private void searchViewSetup() {
        SearchView searchView = getActivity().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    mViewModel.fetchAutoCompleteData(newText, mUserLocation);
                    autoCompleteMarkersOnMap();
                } else if (newText.length() == 0) {
                    mViewModel.setAutoCompleteDataLiveDataAsNull();
                    nearbySearchMarkersOnMap();
                }
                return false;
            }
        });
    }

    private void autoCompleteMarkersOnMap() {
        mGoogleMap.clear();
        BitmapDescriptor markerIcon = setUpMapIcon();

        mViewModel.getAutoCompleteLiveData().observe(getViewLifecycleOwner(), new Observer<MyAutoCompleteData>() {
            @Override
            public void onChanged(MyAutoCompleteData myAutoCompleteData) {
                if (myAutoCompleteData != null) {
                    mPredictionList = myAutoCompleteData.getPredictions();

                    for (Result result : nearBySearchResultList) {
                        for (Prediction prediction : mPredictionList) {
                            if (prediction.getStructuredFormatting().getMainText().contains(result.getName())) {
                                LatLng restauLatLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                                mGoogleMap.addMarker(new MarkerOptions().icon(markerIcon).position(restauLatLng).title(result.getName()));
                            }
                        }
                    }
                }

            }
        });
    }

    private BitmapDescriptor setUpMapIcon() {
        Drawable markerIconDrawable = getResources().getDrawable(R.drawable.ic_baseline_local_dining_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable);
        DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.orange));
        return mViewModel.makeDrawableIntoBitmap(wrappedDrawable);
    }

}