package ca.aequilibrium.bbweather.models;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private int volume;

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rain rain = (Rain) o;

        return volume == rain.volume;
    }

    @Override
    public int hashCode() {
        return volume;
    }
}
