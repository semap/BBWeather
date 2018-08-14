package ca.aequilibrium.bbweather.utils.asynctasks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class SaveBookmarkedCitiesAsyncTask extends CallbackAsyncTask<List<BookmarkedCity>, Void, List<BookmarkedCity>> {
    private static final String TAG = SaveBookmarkedCitiesAsyncTask.class.getSimpleName();
    private Context context;
    private String filename;

    public SaveBookmarkedCitiesAsyncTask(Context context, String filename, ResultCallback<List<BookmarkedCity>> resultCallback) {
        super(resultCallback);
        this.filename = filename;
        this.context = context;
    }

    @Override
    protected TaskResult<List<BookmarkedCity>> doInBackground(List<BookmarkedCity>... lists) {
        if (lists == null) {
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

