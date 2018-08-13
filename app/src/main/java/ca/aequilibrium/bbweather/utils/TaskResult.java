package ca.aequilibrium.bbweather.utils;

public class TaskResult<T> {
    public final T result;
    public final Exception error;

    public TaskResult(T result) {
        this.result = result;
        this.error  = null;
    }

    public TaskResult(Exception error) {
        this.result = null;
        this.error  = error;
    }
}