package ca.aequilibrium.bbweather.utils;


import android.support.annotation.NonNull;

import ca.aequilibrium.bbweather.utils.TaskResult;

public interface ResultCallback<T> {
    void callback(@NonNull TaskResult<T> t);
}