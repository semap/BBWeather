package ca.aequilibrium.bbweather.managers;

import android.graphics.Bitmap;

import ca.aequilibrium.bbweather.utils.ResultCallback;

public interface ImageManager {
    void getImage(String url, ResultCallback<Bitmap> resultCallback);

    void getWeatherImage(String name, ResultCallback<Bitmap> resultCallback);
}
