package ca.aequilibrium.bbweather.models;

import com.google.gson.annotations.SerializedName;

public class Snow {
    @SerializedName("3h")
    private double volume;

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snow snow = (Snow) o;

        return Double.compare(snow.volume, volume) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(volume);
        return (int) (temp ^ (temp >>> 32));
    }
}
