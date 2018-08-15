package ca.aequilibrium.bbweather.utils;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public abstract class SharedPreferenceLiveData<T> extends LiveData<T> {
    protected SharedPreferences sharedPrefs;
    private String key;
    private T defaultValue;

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (SharedPreferenceLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(SharedPreferenceLiveData.this.key, defaultValue));
            }
        }
    };

    private SharedPreferenceLiveData(SharedPreferences sharedPrefs, @NonNull String key, T defaultValue) {
        super();
        this.sharedPrefs = sharedPrefs;
        this.key = key;
        this.defaultValue = defaultValue;
        postValue(getValueFromPreferences(key, defaultValue));
    }

    public static SharedPreferenceLiveData<Boolean> booleanLiveData(SharedPreferences prefs, String key, boolean defaultValue) {
        return new BooleanPreference(prefs, key, defaultValue);
    }

    protected abstract T getValueFromPreferences(String key, T defaultValue);

    @Override
    public void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key, defaultValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }

    private static class BooleanPreference extends SharedPreferenceLiveData<Boolean> {

        private BooleanPreference(SharedPreferences sharedPrefs, @NonNull String key, Boolean defaultValue) {
            super(sharedPrefs, key, defaultValue);
        }

        @Override
        public Boolean getValueFromPreferences(String key, Boolean defaultValue) {
            return sharedPrefs.getBoolean(key, defaultValue);
        }
    }
}
