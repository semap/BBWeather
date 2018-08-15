package ca.aequilibrium.bbweather;

import android.app.Application;
import android.content.Context;

public class BBWeatherApplication extends Application {
    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
