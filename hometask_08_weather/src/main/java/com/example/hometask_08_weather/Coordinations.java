package com.example.hometask_08_weather;

public class Coordinations {
    private double lon ;
    private double lat ;

    Coordinations(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    double getLon() {
        return lon;
    }

    double getLat() {
        return lat;
    }
}
