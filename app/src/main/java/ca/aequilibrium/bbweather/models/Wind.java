package ca.aequilibrium.bbweather.models;

import java.util.Objects;

public class Wind {
    private double deg;
    private double speed;

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wind wind = (Wind) o;
        return Double.compare(wind.deg, deg) == 0 &&
                Double.compare(wind.speed, speed) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(deg, speed);
    }
}
