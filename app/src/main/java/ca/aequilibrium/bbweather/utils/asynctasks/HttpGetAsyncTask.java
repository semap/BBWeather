package ca.aequilibrium.bbweather.utils.asynctasks;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class HttpGetAsyncTask<T> extends CallbackAsyncTask<URL, Void, T> {
    private static final String TAG = HttpGetAsyncTask.class.getSimpleName();
    private Class clazz;

    public HttpGetAsyncTask(Class clazz, ResultCallback<T> resultCallback) {
        super(resultCallback);
        this.clazz = clazz;
    }


    @Override
    protected TaskResult<T> doInBackground(URL... urls) {
        if (urls == null || urls.length == 0) {
            Exception exception = new IllegalArgumentException("No URL");
            return new TaskResult<T>(exception);
        }
        InputStream stream = null;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) urls[0].openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            stream = connection.getInputStream();

            return new TaskResult<>(deserialize(stream));

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                    Log.e(TAG, "Unable to close BufferReader", e);
                }
            }
            return new TaskResult<T>(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected T deserialize(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream)));
        return (T) new Gson().fromJson(br, clazz);
    }

}
