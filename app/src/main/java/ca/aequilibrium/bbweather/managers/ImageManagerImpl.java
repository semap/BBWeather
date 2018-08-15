package ca.aequilibrium.bbweather.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import ca.aequilibrium.bbweather.R;
import ca.aequilibrium.bbweather.utils.ImageCache;
import ca.aequilibrium.bbweather.utils.ResultCallback;
import ca.aequilibrium.bbweather.utils.TaskResult;
import ca.aequilibrium.bbweather.utils.asynctasks.GetBitmapAsyncTask;


public class ImageManagerImpl implements ImageManager {
    private static final String TAG = ImageManagerImpl.class.getSimpleName();

    private ImageCache cache;
    private Context context;

    public ImageManagerImpl(Context context) {
        this.context = context;
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
        Log.d(TAG, "cache size:" + cacheSize);
        this.cache = new ImageCache(cacheSize);
    }

    @Override
    public void getImage(final String urlString, final ResultCallback<Bitmap> resultCallback) {

        Bitmap cachedBitmap = cache.get(urlString);
        if (cachedBitmap != null) {
            resultCallback.callback(new TaskResult<>(cachedBitmap));
            return;
        }

        try {
            final GetBitmapAsyncTask task = new GetBitmapAsyncTask(new ResultCallback<Bitmap>() {
                @Override
                public void callback(@NonNull TaskResult<Bitmap> taskResult) {
                    if (taskResult.result != null) {
                        cache.put(urlString, taskResult.result);
                    }
                    if (resultCallback != null) {
                        resultCallback.callback(taskResult);
                    }
                }
            });

            URL url = new URL(urlString);

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Unable to create forecast URL for weather API");
            if (resultCallback != null) {
                resultCallback.callback(new TaskResult<Bitmap>(e));
            }
        }

    }

    @Override
    public void getWeatherImage(final String name, final ResultCallback<Bitmap> resultCallback) {
        String url = context.getString(R.string.open_weather_map_icon_base_url) + "/" + name + ".png";
        getImage(url, resultCallback);
    }
}
