package ca.aequilibrium.bbweather.models;

public class Wind {
    private double deg;
    private double speed;

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wind wind = (Wind) o;

        if (Double.compare(wind.deg, deg) != 0) return false;
        return Double.compare(wind.speed, speed) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(deg);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(speed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
