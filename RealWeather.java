package com.linghong.molian.bean;

import java.io.Serializable;

/**
 * 天气
 */
public class RealWeather implements Serializable {
    private WeatherData weatherinfo;

    public WeatherData getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherData weatherinfo) {
        this.weatherinfo = weatherinfo;
    }
}
