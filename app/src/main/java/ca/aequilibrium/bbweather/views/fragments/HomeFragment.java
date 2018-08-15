package ca.aequilibrium.bbweather.views.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest.permission;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.viewmodels.HomeViewModel;
import ca.aequilibrium.bbweather.views.adapters.BookmarkedCitiesAdapter;
import ca.aequilibrium.bbweather.views.adapters.BookmarkedCitiesAdapterListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, BookmarkedCitiesAdapterListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private MapView mapView;
    private GoogleMap googleMap;
    private HomeViewModel homeViewModel;
    private BookmarkedCitiesAdapter bookmarkedCitiesAdapter;

    private static final int REQUEST_LOCATION_PERMISSIONS = 102;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        bookmarkedCitiesAdapter = new BookmarkedCitiesAdapter();
        bookmarkedCitiesAdapter.setListener(this);

        RecyclerView bookmarkedList = view.findViewById(R.id.bookmarked_cities);
        bookmarkedList
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bookmarkedList.setAdapter(bookmarkedCitiesAdapter);

        subscribeToViewModel();
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
        this.googleMap = googleMap;
        requestPermissionIfNecessary();
        this.googleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onMapLongClick(final LatLng latLng) {
        Log.d(TAG, "user long clicks on the map");

        // the logic of parsing the latitude and longitude should not be here in the presentation layer
        Coord coord = new Coord(latLng.latitude, latLng.longitude);
        homeViewModel.addBookmarkedCityByCoord(coord);
    }

    private void subscribeToViewModel() {
        homeViewModel.getBookmarkedCityObservable().observe(this, new Observer<List<BookmarkedCity>>() {
            @Override
            public void onChanged(@Nullable List<BookmarkedCity> bookmarkedCities) {
                if (bookmarkedCities == null) {
                    Log.e(TAG, "unable to load bookmarked cities");
                } else {
                    bookmarkedCitiesAdapter.setBookmarkedCities(bookmarkedCities);
                    Log.i(TAG, "count: " +  bookmarkedCities.size());
                }
            }
        });

        homeViewModel.getMessageObservable().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(@Nullable Message message) {
                Toast.makeText(getContext(), message.getBody(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBookmarkedCityClicked(BookmarkedCity bookmarkedCity) {
        FragmentManager fragmentManager = getFragmentManager();

        CityFragment cityFragment = (CityFragment) fragmentManager.findFragmentByTag(CityFragment.TAG);
        if (cityFragment == null) {
            cityFragment = new CityFragment();
        }

        cityFragment.setCoord(bookmarkedCity.getCoord());
        fragmentManager.beginTransaction()
                .add(R.id.homeRoot, cityFragment, CityFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRemoveBookMarkCityButtonClicked(BookmarkedCity bookmarkedCity) {
        homeViewModel.removeBookmarkedCity(bookmarkedCity);
    }
}
