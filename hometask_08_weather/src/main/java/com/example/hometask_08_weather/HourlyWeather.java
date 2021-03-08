package com.example.hometask_08_weather;

class HourlyWeather {

    private String time ;
    private String degreesCelcius ;
    private String degreesFahrenheit ;
    private String description ;
    private String iconUrl ;

    HourlyWeather(String time, String degreesCelcius, String degreesFahrenheit, String description, String iconUrl) {
        this.time = time;
        this.degreesCelcius = degreesCelcius;
        this.degreesFahrenheit = degreesFahrenheit;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    String getTime() {
        return time;
    }

    String getDegreesCelcius() {
        return degreesCelcius;
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
