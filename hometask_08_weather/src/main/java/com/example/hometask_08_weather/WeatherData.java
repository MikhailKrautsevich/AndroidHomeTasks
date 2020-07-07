package com.example.hometask_08_weather;

public class WeatherData {
    private double temperature ;
    private String description ;
    private String iconURL ;
    private String date ;

    WeatherData(double temperature, String description, String iconURL, String date) {
        this.temperature = temperature;
        this.description = description;
        this.iconURL = iconURL;
        this.date = date;
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

    String getDate() {
        return date ;
    }
}
