package ca.aequilibrium.bbweather.utils.asynctasks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.aequilibrium.bbweather.models.BookmarkedCity;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

/**
 * Async task to load the bookmarked cities
 */
public class LoadBookmarkedCitiesAsyncTask extends CallbackAsyncTask<String, Void, List<BookmarkedCity>> {
    private static final String TAG = LoadBookmarkedCitiesAsyncTask.class.getSimpleName();
    private Context context;

    public LoadBookmarkedCitiesAsyncTask(Context context, ResultCallback<List<BookmarkedCity>> resultCallback) {
        super(resultCallback);
        this.context = context;
    }

    @Override
    protected TaskResult<List<BookmarkedCity>> doInBackground(String... strings) {
        if (strings == null || strings.length == 0) {
            List<BookmarkedCity> result = new ArrayList<>();
            return new TaskResult<>(result);
        }

        String filename = strings[0];
        FileInputStream fileInputStream = null;

        try {
            StringBuilder text = new StringBuilder();
            fileInputStream = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(fileInputStream)));

            Gson gson = new Gson();
            Type listType = new TypeToken<List<BookmarkedCity>>() {}.getType();

            List<BookmarkedCity> list = gson.fromJson(br, listType);
            return new TaskResult<>(list);
        } catch (FileNotFoundException fnfe) {
            Log.d(TAG, "Bookmark file does not exist.");
            List<BookmarkedCity> result = new ArrayList<>();
            return new TaskResult<>(result);
        } finally {

            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "Failed to close the steam.");
                List<BookmarkedCity> result = new ArrayList<>();
                return new TaskResult<>(result);
            }

        }

    }
}