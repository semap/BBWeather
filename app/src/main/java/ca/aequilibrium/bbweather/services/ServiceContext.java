package ca.aequilibrium.bbweather.services;


import ca.aequilibrium.bbweather.BBWeatherApplication;

public class ServiceContext {
    public static BookmarkedLocationService bookmarkedLocationService =
            new BookmarkedLocationServiceImpl(BBWeatherApplication.getAppContext());
    public static WeatherService weatherService =
            new WeatherServiceImpl(BBWeatherApplication.getAppContext());
}
