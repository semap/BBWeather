package ca.aequilibrium.bbweather.utils;


import ca.aequilibrium.bbweather.utils.TaskResult;

public interface ResultCallback<T> {
    void callback(TaskResult<T> t);
}