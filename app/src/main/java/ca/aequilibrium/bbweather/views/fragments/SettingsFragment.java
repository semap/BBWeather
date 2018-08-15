package ca.aequilibrium.bbweather.views.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.utils.Message;
import ca.aequilibrium.bbweather.viewmodels.CityViewModel;
import ca.aequilibrium.bbweather.viewmodels.SettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Switch unitSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        unitSwitch = view.findViewById(R.id.use_metric_switch);

        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                settingsViewModel.setIsMetric(isChecked);
            }
        });

        view.findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                settingsViewModel.removeAllBookmarkedCities();
            }
        });

        subscribeToViewModel();

        return view;
    }

    private void subscribeToViewModel() {
        settingsViewModel.getIsMetricObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isMetric) {
                unitSwitch.setChecked(isMetric);
            }
        });

        settingsViewModel.getMessageObservable().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(@Nullable Message message) {
                Toast.makeText(getContext(), message.getBody(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
