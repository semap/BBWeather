package ca.aequilibrium.bbweather.managers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.models.ForecastInfo;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;
import ca.aequilibrium.bbweather.utils.asynctasks.HttpGetAsyncTask;

public class WeatherManagerImpl implements WeatherManager {

    private static final String TAG = WeatherManagerImpl.class.getSimpleName();

    // the Context is for getting the URL info
    private Context context;

    public WeatherManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getCurrentWeather(final Coord coord, final Boolean isMetric, final ResultCallback<CurrentWeather> resultCallback) {
        try {
            URL url = buildURL(coord, isMetric, context.getString(R.string.open_weather_map_current_weather_path));
            HttpGetAsyncTask<CurrentWeather> request = new HttpGetAsyncTask<>(CurrentWeather.class, resultCallback);
            request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Unable to create weather URL for weather API");
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<CurrentWeather>(e));
            }
        }
    }

    @Override
    public void getForecasts(Coord coord, Boolean isMetric, ResultCallback<ForecastInfo> resultCallback) {
        try {
            URL url = buildURL(coord, isMetric, context.getString(R.string.open_weather_map_forecast_path));
            HttpGetAsyncTask<ForecastInfo> request = new HttpGetAsyncTask<>(ForecastInfo.class, resultCallback);
            request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Unable to create forecast URL for weather API");
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<ForecastInfo>(e));
            }
        }
    }

    private URL buildURL(final Coord coord, final Boolean isMetric, String path) throws MalformedURLException {
        Uri.Builder builtUri = Uri.parse(context.getString(R.string.open_weather_map_base_url))
                .buildUpon()
                .appendPath(path)
                .appendQueryParameter("appid", context.getString(R.string.open_weather_map_api_key))
                .appendQueryParameter("lat", String.valueOf(coord.getLat()))
                .appendQueryParameter("lon", String.valueOf(coord.getLon()))
                .appendQueryParameter("units", (isMetric == null || isMetric) ? "metric" : "imperial");

        return new URL(builtUri.build().toString());
    }

}