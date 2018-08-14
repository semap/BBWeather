package ca.aequilibrium.bbweather.models;

import java.util.ArrayList;
import java.util.List;

public class Forecast {
    private long dt;        // Time of data forecasted, unix, UTC
    private Main main;
    private List<Weather> weather = new ArrayList<>();
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Snow snow;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Forecast forecast = (Forecast) o;

        if (dt != forecast.dt) return false;
        if (main != null ? !main.equals(forecast.main) : forecast.main != null) return false;
        if (weather != null ? !weather.equals(forecast.weather) : forecast.weather != null)
            return false;
        if (clouds != null ? !clouds.equals(forecast.clouds) : forecast.clouds != null)
            return false;
        if (wind != null ? !wind.equals(forecast.wind) : forecast.wind != null) return false;
        if (rain != null ? !rain.equals(forecast.rain) : forecast.rain != null) return false;
        return snow != null ? snow.equals(forecast.snow) : forecast.snow == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (dt ^ (dt >>> 32));
        result = 31 * result + (main != null ? main.hashCode() : 0);
        result = 31 * result + (weather != null ? weather.hashCode() : 0);
        result = 31 * result + (clouds != null ? clouds.hashCode() : 0);
        result = 31 * result + (wind != null ? wind.hashCode() : 0);
        result = 31 * result + (rain != null ? rain.hashCode() : 0);
        result = 31 * result + (snow != null ? snow.hashCode() : 0);
        return result;
    }
}
