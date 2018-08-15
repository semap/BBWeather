package ca.aequilibrium.bbweather.utils.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ca.aequilibrium.bbweather.utils.ResultCallback;

public class GetBitmapAsyncTask extends HttpGetAsyncTask<Bitmap>{
    public GetBitmapAsyncTask(ResultCallback<Bitmap> resultCallback) {
        super(Bitmap.class, resultCallback);
    }

    protected Bitmap deserialize(InputStream stream) throws IOException {
        return BitmapFactory.decodeStream(stream);
    }
}
