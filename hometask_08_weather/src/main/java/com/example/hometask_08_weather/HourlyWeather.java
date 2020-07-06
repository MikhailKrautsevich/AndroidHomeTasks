package com.example.hometask_08_weather;

public class HourlyWeather {
    private String time ;
    private String degreesCelcius ;
    private String degreesFahrenheit ;
    private String description ;

    public HourlyWeather(String time, String degreesCelcius, String degreesFahrenheit, String description) {
        this.time = time;
        this.degreesCelcius = degreesCelcius;
        this.degreesFahrenheit = degreesFahrenheit;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public String getDegreesCelcius() {
        return degreesCelcius;
    }

    public String getDegreesFahrenheit() {
        return degreesFahrenheit;
    }

    public String getDescription() {
        return description;
    }
}
