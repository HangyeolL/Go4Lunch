package com.hangyeollee.go4lunch.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.FragmentGoogleMapsBinding;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.MyNearBySearchData;
import com.hangyeollee.go4lunch.model.neaerbyserachpojo.Result;
import com.hangyeollee.go4lunch.viewmodel.GoogleMapsFragmentViewModel;
import com.hangyeollee.go4lunch.viewmodel.ViewModelFactory;

public class GoogleMapsFragment extends Fragment {

    private String mLocation;

    private GoogleMap mGoogleMap;
    private GoogleMapsFragmentViewModel mViewModel;

    private FragmentGoogleMapsBinding binding;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGoogleMapsBinding.inflate(inflater, container, false);
        Log.i("GoogleMapsFragment", "onCreateView launched");

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getActivity())).get(GoogleMapsFragmentViewModel.class);

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher, as an instance variable.
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.

            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "Is already granted");

            binding.buttonRequestLocation.setOnClickListener(i -> mViewModel.startLocationRequest());

            mViewModel.getLocationLiveData().observe(getActivity(), new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    mLocation = location.getLatitude() + "," + location.getLongitude();
                    Log.i("MyLocation", mLocation);

                    mViewModel.getNearBySearchLiveData(mLocation).observe(getViewLifecycleOwner(), new Observer<MyNearBySearchData>() {
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
            });

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mGoogleMap = googleMap;
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        mGoogleMap.clear();
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                });
            }

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

//        mViewModel.getIsLoadingLiveData().observe(getActivity(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                    binding.progressBar.setVisibility(View.VISIBLE);
//                } else {
//                    binding.progressBar.setVisibility(View.GONE);
//                }
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.stopLocationRequest();
        binding = null;
    }

    private BitmapDescriptor setUpMapIcon() {
        Drawable markerIconDrawable = getResources().getDrawable(R.drawable.ic_baseline_local_dining_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(markerIconDrawable);
        DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.orange));
        BitmapDescriptor markerIcon = mViewModel.makeDrawableIntoBitmap(wrappedDrawable);
        return markerIcon;
    }


}