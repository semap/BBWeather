package ca.aequilibrium.bbweather.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;


import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.views.fragments.HelpFragment;
import ca.aequilibrium.bbweather.views.fragments.HomeFragment;
import ca.aequilibrium.bbweather.views.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SELECTED_NAV_ID = "selected_nav_id";
    private Fragment currentFragment;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setFragment(item.getItemId());
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        int seletedFragmentId = R.id.navigation_home;
        if (savedInstanceState != null) {
            seletedFragmentId = savedInstanceState.getInt(SELECTED_NAV_ID, seletedFragmentId);
        }
        setFragment(seletedFragmentId);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_NAV_ID, navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    private void setFragment(final int itemId) {
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .commit();
        }
        Fragment nextFragment = null;
        switch (itemId) {
            case R.id.navigation_home:
                nextFragment = getHomeFragment();
                break;
            case R.id.navigation_settings:
                nextFragment = getSettingsFragment();
                break;
            case R.id.navigation_help:
                nextFragment = getSettingsFragment();
                break;
        }
        if (nextFragment != null) {
            showFragment(nextFragment);
            currentFragment = nextFragment;
        }
    }

    private Fragment getHomeFragment() {
        String tag = "home";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new HomeFragment();
            fragment.setRetainInstance(true);
            addFragment(fragment, tag);
        }
        return fragment;
    }

    private Fragment getSettingsFragment() {
        String tag = "settings";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new SettingsFragment();
            addFragment(fragment, tag);
        }
        return fragment;
    }

    private Fragment getHelpFragment() {
        String tag = "help";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new HelpFragment();
            addFragment(fragment, tag);
        }
        return fragment;
    }

    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment, tag)
                .commit();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

}
