package ca.aequilibrium.bbweather.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.util.List;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.viewmodels.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subscribeToBookmarkedLocations();

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void subscribeToBookmarkedLocations() {
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        viewModel.getBookmarkedCityObservable().observe(this, new Observer<List<BookmarkedCity>>() {
            @Override
            public void onChanged(@Nullable List<BookmarkedCity> bookmarkedCities) {
                Log.i(TAG, "count: " +  bookmarkedCities.size());

            }
        });

        viewModel.loadBookmarkedLocations();

    }

}
