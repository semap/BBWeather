package ca.aequilibrium.bbweather.managers;


import ca.aequilibrium.bbweather.BBWeatherApplication;

public class ManagerContext {
    public static BookmarkedLocationManager bookmarkedLocationManager =
            new BookmarkedLocationManagerImpl(BBWeatherApplication.getAppContext());
    public static WeatherManager weatherManager =
            new WeatherManagerImpl(BBWeatherApplication.getAppContext());

    public static ImageManager imageManager = new ImageManagerImpl(BBWeatherApplication.getAppContext());
}
