package ca.aequilibrium.bbweather.utils.asynctasks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class SaveBookmarkedCitiesAsyncTask extends CallbackAsyncTask<List<BookmarkedCity>, Void, List<BookmarkedCity>> {
    private static final String TAG = SaveBookmarkedCitiesAsyncTask.class.getSimpleName();
    private WeakReference<Context> contextRef;
    private String filename;

    public SaveBookmarkedCitiesAsyncTask(Context context, String filename, ResultCallback<List<BookmarkedCity>> resultCallback) {
        super(resultCallback);
        this.filename = filename;
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    protected TaskResult<List<BookmarkedCity>> doInBackground(List<BookmarkedCity>... lists) {
        Context context = contextRef.get();
        if (lists == null || context == null) {
            Exception exception = new IllegalArgumentException("Can't save the null bookmarked city list.");
            return new TaskResult<List<BookmarkedCity>>(exception);
        }

        List<BookmarkedCity> list = lists[0];
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            String json = new Gson().toJson(list);
            outputStream.write(json.getBytes());
            return new TaskResult<>(list);
        } catch (IOException e) {
            Log.e(TAG, "failed to save the bookmarked city into the local file.", e);
            return new TaskResult<>(e);
        } finally {

            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "Failed to close the steam.");
                return new TaskResult<>(e);
            }
        }

    }
}

