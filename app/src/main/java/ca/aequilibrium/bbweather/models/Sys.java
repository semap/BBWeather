package ca.aequilibrium.bbweather.models;

public class Sys {
    private String country;
    private double sunrise;
    private double sunset;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSunrise() {
        return sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public double getSunset() {
        return sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sys sys = (Sys) o;

        if (Double.compare(sys.sunrise, sunrise) != 0) return false;
        if (Double.compare(sys.sunset, sunset) != 0) return false;
        return country != null ? country.equals(sys.country) : sys.country == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = country != null ? country.hashCode() : 0;
        temp = Double.doubleToLongBits(sunrise);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(sunset);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
