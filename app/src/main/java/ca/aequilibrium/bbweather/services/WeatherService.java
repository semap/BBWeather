package ca.aequilibrium.bbweather.services;

import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.utils.ResultCallback;

public interface WeatherService {
    void getCurrentWeather(Coord coord, ResultCallback<CurrentWeather> resultCallback);
}
