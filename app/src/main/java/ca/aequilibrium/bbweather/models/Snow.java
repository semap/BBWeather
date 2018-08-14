package ca.aequilibrium.bbweather.models;

import com.google.gson.annotations.SerializedName;

public class Snow {
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

        Snow snow = (Snow) o;

        return volume == snow.volume;
    }

    @Override
    public int hashCode() {
        return volume;
    }
}
