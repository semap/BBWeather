package ca.aequilibrium.bbweather.utils.asynctasks;

import android.os.AsyncTask;

import java.net.URL;

import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

/**
 * A aync task with a callback. When the task is done, it will invoke the callback with the result including the status.
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class CallbackAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, TaskResult<Result>> {

    protected ResultCallback<Result> resultCallback;

    public CallbackAsyncTask(ResultCallback<Result> resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    protected void onPostExecute(TaskResult<Result> result) {
        super.onPostExecute(result);
        if (resultCallback != null) {
            resultCallback.callback(result);
        }
    }
}
