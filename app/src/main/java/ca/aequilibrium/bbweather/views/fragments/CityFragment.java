package ca.aequilibrium.bbweather.views.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.viewmodels.CityViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    public static final String TAG = CityFragment.class.getSimpleName();

    private CityViewModel cityViewModel;
    private Toolbar toolbar;
    private ImageView collapsedIcon;
    private Coord coord;

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        collapsedIcon = view.findViewById(R.id.collapsing_icon);

        cityViewModel = ViewModelProviders.of(this).get(CityViewModel.class);
        subscribeToCurrentWeather();

        cityViewModel.getMessageObservable().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(@Nullable Message message) {
                Toast.makeText(getContext(), message.getBody(), Toast.LENGTH_SHORT).show();
            }
        });

        cityViewModel.setCoord(coord);
        setupToolbar();
        return view;
    }

    private void subscribeToCurrentWeather() {
        cityViewModel.getCurrentWeatherObservable().observe(this, new Observer<CurrentWeather>() {
            @Override
            public void onChanged(@Nullable CurrentWeather currentWeather) {
                updateWeatherUI(currentWeather);
            }
        });

        cityViewModel.getWeatherIconObservable().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                collapsedIcon.setImageBitmap(bitmap);
            }
        });
    }

    private void updateWeatherUI(CurrentWeather currentWeather) {
        toolbar.setTitle(currentWeather.getName());
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

}
