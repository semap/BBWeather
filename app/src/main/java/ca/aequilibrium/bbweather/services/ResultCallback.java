package ca.aequilibrium.bbweather.services;


import ca.aequilibrium.bbweather.utils.TaskResult;

public interface ResultCallback<T> {
    void callback(TaskResult<T> t);
}