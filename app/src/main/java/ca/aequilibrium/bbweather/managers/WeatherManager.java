package ca.aequilibrium.bbweather.managers;

import ca.aequilibrium.bbweather.models.Coord;
import ca.aequilibrium.bbweather.models.CurrentWeather;
import ca.aequilibrium.bbweather.models.ForecastInfo;
import ca.aequilibrium.bbweather.utils.ResultCallback;

public interface WeatherManager {
    void getCurrentWeather(Coord coord, Boolean isMetric, ResultCallback<CurrentWeather> resultCallback);

    void getForecasts(Coord coord, Boolean isMetric, ResultCallback<ForecastInfo> resultCallback);
}
