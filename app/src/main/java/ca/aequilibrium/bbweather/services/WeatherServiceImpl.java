package ca.aequilibrium.bbweather.services;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.utils.HttpGetRequest;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;

public class WeatherServiceImpl implements WeatherService {

    private static final String TAG = WeatherServiceImpl.class.getSimpleName();

    // the Context is for getting the URL info
    private Context context;

    public WeatherServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getCurrentWeather(final Coord coord, final ResultCallback<CurrentWeather> resultCallback) {

        try {
            URL url = buildURL(coord);
            HttpGetRequest<CurrentWeather> request = new HttpGetRequest<>(CurrentWeather.class, resultCallback);
            request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Unable to create URL for weather API");
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<CurrentWeather>(e));
            }
        }
    }

    private URL buildURL(final Coord coord) throws MalformedURLException {
        Uri.Builder builtUri = Uri.parse(context.getString(R.string.open_weather_map_base_url))
                .buildUpon()
                .appendPath(context.getString(R.string.open_weather_map_current_weather_path))
                .appendQueryParameter("appid", context.getString(R.string.open_weather_map_api_key))
                .appendQueryParameter("lat", String.valueOf(coord.getLat()))
                .appendQueryParameter("lon", String.valueOf(coord.getLon()))
                .appendQueryParameter("units", "metric");

        return new URL(builtUri.build().toString());
    }

}