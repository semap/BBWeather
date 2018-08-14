package ca.aequilibrium.bbweather.views.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest.permission;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Location;
import ca.aequilibrium.bbweather.viewmodels.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private MapView mMapView;
    private GoogleMap mMap;
    private HomeViewModel mHomeViewModel;

    private static final int REQUEST_LOCATION_PERMISSIONS = 102;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        subscribeToBookmarkedLocations();
        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    private void createMap() {
        requestPermissionIfNecessary();

    }

    private void requestPermissionIfNecessary() {
        if (ActivityCompat.checkSelfPermission(getContext(), permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[] { permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS
            );
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPermissionIfNecessary();
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onMapLongClick(final LatLng latLng) {
        Log.d(TAG, "user long clicks on the map");

        // the logic of parsing the latitude and longitude should not be here in the presentation layer
        mHomeViewModel.addBookmarkedLocationByLocation(new Location(latLng.latitude, latLng.longitude));
    }

    private void subscribeToBookmarkedLocations() {
        mHomeViewModel.getBookmarkedCityObservable().observe(this, new Observer<List<BookmarkedCity>>() {
            @Override
            public void onChanged(@Nullable List<BookmarkedCity> bookmarkedCities) {
                if (bookmarkedCities == null) {
                    Log.e(TAG, "unable to load bookmarked cities");
                } else {
                    Log.i(TAG, "count: " +  bookmarkedCities.size());
                }


            }
        });

    }
}
