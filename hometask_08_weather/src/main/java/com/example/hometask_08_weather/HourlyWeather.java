package com.example.hometask_08_weather;

class HourlyWeather {

    private String time ;
    private String degreesCelsius;
    private String degreesFahrenheit ;
    private String description ;
    private String iconUrl ;

    HourlyWeather(String time, String degreesCelcius, String degreesFahrenheit, String description, String iconUrl) {
        this.time = time;
        this.degreesCelsius = degreesCelcius;
        this.degreesFahrenheit = degreesFahrenheit;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    String getTime() {
        return time;
    }

    String getDegreesCelsius() {
        return degreesCelsius;
    }

    String getDegreesFahrenheit() {
        return degreesFahrenheit;
    }

    String getDescription() {
        return description;
    }

    String getIconUrl() {
        return iconUrl;
    }
}
