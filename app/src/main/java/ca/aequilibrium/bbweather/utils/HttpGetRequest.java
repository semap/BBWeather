package ca.aequilibrium.bbweather.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpGetRequest<T> extends AsyncTask<URL, Void, TaskResult<T>>  {
    private static final String TAG = HttpGetRequest.class.getSimpleName();
    private Class clazz;
    private ResultCallback<T> resultCallback;

    public HttpGetRequest(Class clazz, ResultCallback<T> resultCallback) {
        this.clazz = clazz;
        this.resultCallback = resultCallback;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TaskResult<T> doInBackground(URL... urls) {
        if (urls == null || urls.length == 0) {
            Exception exception = new IllegalArgumentException("No URL");
            return new TaskResult<T>(exception);
        }
        BufferedReader br = null;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) urls[0].openConnection();
            connection.setReadTimeout(50000);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            InputStream stream = connection.getInputStream();

            br = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream)));
            T result =  (T) new Gson().fromJson(br, clazz);
            return new TaskResult<>(result);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);

            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    Log.e(TAG, "Unable to close BufferReader", e);
                }
            }
            return new TaskResult<T>(e);
        }
    }

    @Override
    protected void onPostExecute(TaskResult<T> result) {
        super.onPostExecute(result);
        if (resultCallback != null) {
            resultCallback.callback(result);
        }
    }
}
