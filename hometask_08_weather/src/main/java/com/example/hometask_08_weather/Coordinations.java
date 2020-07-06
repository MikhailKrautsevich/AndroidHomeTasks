package com.example.hometask_08_weather;

public class Coordinations {
    private double lon ;
    private double lat ;

    public Coordinations(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
