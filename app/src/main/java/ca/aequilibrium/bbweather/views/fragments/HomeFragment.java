package ca.aequilibrium.bbweather.views.fragments;


import android.Manifest.permission;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;
import ca.aequilibrium.bbweather.viewmodels.HomeViewModel;
import ca.aequilibrium.bbweather.views.adapters.BookmarkedCitiesAdapter;
import ca.aequilibrium.bbweather.views.adapters.BookmarkedCitiesAdapterListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, BookmarkedCitiesAdapterListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private static final int REQUEST_LOCATION_PERMISSIONS = 102;
    private Button searchButton;
    private EditText searchEditText;
    private MapView mapView;
    private GoogleMap googleMap;
    private HomeViewModel homeViewModel;
    private BookmarkedCitiesAdapter bookmarkedCitiesAdapter;
    private Map<String, Marker> markerMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        clearMapMarkers();
        mapView.getMapAsync(this);

        searchEditText = view.findViewById(R.id.search_text);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT){
                    onMapSearch(textView.getText().toString());
                }
                return false;
            }
        });
        searchButton = view.findViewById(R.id.map_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapSearch(searchEditText.getText().toString());
            }
        });
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        bookmarkedCitiesAdapter = new BookmarkedCitiesAdapter();
        bookmarkedCitiesAdapter.setListener(this);

        RecyclerView bookmarkedList = view.findViewById(R.id.bookmarked_cities);
        bookmarkedList
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bookmarkedList.setAdapter(bookmarkedCitiesAdapter);

        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        subscribeToViewModel();
        this.googleMap.setOnMapLongClickListener(this);

        if (ActivityCompat.checkSelfPermission(getContext(), permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .zoom(8.0f)
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

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
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(googleMap);
            }
        }
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
                    updateMapMarkers(bookmarkedCities);
                    Log.i(TAG, "count: " + bookmarkedCities.size());
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
                .addToBackStack(CityFragment.TAG)
                .commit();
    }

    @Override
    public void onRemoveBookMarkCityButtonClicked(BookmarkedCity bookmarkedCity) {
        homeViewModel.removeBookmarkedCity(bookmarkedCity);
    }

    private void updateMapMarkers(List<BookmarkedCity> bookmarkedCities) {
        if (googleMap == null || bookmarkedCities == null) {
            return;
        }

        if (bookmarkedCities.isEmpty()) {
            clearMapMarkers();
        } else {

            Set<String> bookmarkedCityIds = new HashSet<>();
            bookmarkedCityIds.addAll(markerMap.keySet());

            for (BookmarkedCity bookmarkedCity : bookmarkedCities) {
                bookmarkedCityIds.remove(bookmarkedCity.getId());
                if (!markerMap.containsKey(bookmarkedCity.getId())) {

                    placeMarker(bookmarkedCity);
                }
            }

            // remove old markers
            for (String id: bookmarkedCityIds) {
                Marker marker = markerMap.get(id);
                if (marker != null) {
                    marker.remove();
                }
            }
        }
    }

    private void clearMapMarkers() {
        for (Marker marker : markerMap.values()) {
            marker.remove();
        }
        markerMap.clear();
    }

    private void placeMarker(BookmarkedCity bookmarkedCity) {
        Coord coord = bookmarkedCity.getCoord();
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(new LatLng(coord.getLat(), coord.getLon())));
        marker.setTag(bookmarkedCity);
        markerMap.put(bookmarkedCity.getId(), marker);
    }

    private void onMapSearch(String text) {
        Log.d(TAG, "search " + text);
        hideKeyboard();
        if (text == null) {
            return;
        }

        homeViewModel.getCoordFromName(text, new ResultCallback<Coord>() {
            @Override
            public void callback(@NonNull TaskResult<Coord> taskResult) {
                Coord coord = taskResult.result;
                if (coord != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(coord.getLat(), coord.getLon())));
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
