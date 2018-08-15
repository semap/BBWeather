package ca.aequilibrium.bbweather.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.util.List;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.viewmodels.HomeViewModel;
import ca.aequilibrium.bbweather.views.fragments.CityFragment;
import ca.aequilibrium.bbweather.views.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showHome();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void showHome() {
        Fragment homeFragment = new CityFragment();
        createFragment(homeFragment);
        showFragment(homeFragment);
    }


    private void createFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

}
