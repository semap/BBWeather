package ca.aequilibrium.bbweather.utils;


import android.support.annotation.NonNull;

public interface ResultCallback<T> {
    void callback(@NonNull TaskResult<T> t);
}