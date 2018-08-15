package ca.aequilibrium.bbweather.utils;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

/**
 * LiveData does not have the feature of distinctUntilChanged. Observer can skip the duplicate items.
 * See: http://reactivex.io/documentation/operators/distinct.html
 *
 * @param <T>
 */
public abstract class DistinctUntilChangedObserver<T> implements Observer<T> {

    private T oldValue;

    @Override
    public void onChanged(@Nullable T newValue) {
        if (oldValue == null) {
            if (newValue != null) {
                oldValue = newValue;
                onDistnctChanged(newValue);
            }
        } else {
            if (!oldValue.equals(newValue)) {
                oldValue = newValue;
                onDistnctChanged(newValue);
            }
        }
    }

    public abstract void onDistnctChanged(@Nullable T newValue);
}