package com.example.example_network;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherParser {

    private String jsonData ;

    public WeatherParser(String jsonData) {
        this.jsonData = jsonData;
    }

    public void parseData() throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData) ;
    }
}
