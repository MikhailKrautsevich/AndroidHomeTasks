package com.example.hometask_08_weather;

public class WeatherData {
    private double temperature ;
    private String description ;
    private String iconURL ;

    WeatherData(double temperature, String description, String iconURL) {
        this.temperature = temperature;
        this.description = description;
        this.iconURL = iconURL;
    }

    double getTemperature() {
        return temperature;
    }

    String getDescription() {
        return description;
    }

    String getIconURL() {
        return iconURL;
    }
}
