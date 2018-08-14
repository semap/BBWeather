package ca.aequilibrium.bbweather.models;

import com.google.gson.annotations.SerializedName;

public class Main {
    private double temp;
    private double pressure;
    private double humidity;
    @SerializedName("temp_max")
    private double tempMax;
    @SerializedName("temp_min")
    private double tempMin;
    @SerializedName("sea_level")
    private double seaLevel;
    @SerializedName("grnd_level")
    private double groundLevel;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public double getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(double groundLevel) {
        this.groundLevel = groundLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Main main = (Main) o;

        if (Double.compare(main.temp, temp) != 0) return false;
        if (Double.compare(main.pressure, pressure) != 0) return false;
        if (Double.compare(main.humidity, humidity) != 0) return false;
        if (Double.compare(main.tempMax, tempMax) != 0) return false;
        if (Double.compare(main.tempMin, tempMin) != 0) return false;
        if (Double.compare(main.seaLevel, seaLevel) != 0) return false;
        return Double.compare(main.groundLevel, groundLevel) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        temp1 = Double.doubleToLongBits(temp);
        result = (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(pressure);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(humidity);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(tempMax);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(tempMin);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(seaLevel);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(groundLevel);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }
}
