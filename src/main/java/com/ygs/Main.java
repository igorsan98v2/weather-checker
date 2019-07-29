package com.ygs;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String []args){
        WeatherParser weatherParser= new WeatherParser();
        Map<String,String> queryParams = new HashMap<String, String>();
        queryParams.put("exclude","currently");
        queryParams.put("lang","ru");
        queryParams.put("units","si");
        //Weather weather = null;
        Weather weather= weatherParser.getWeather(48.4322,37.3322,queryParams);
        if(weather!=null){
            System.out.println("Температура твоєї москальскої дупи: "+weather.getHourly().getData().get(0).getTemperature());
        }
        else {
            System.out.println("LOSER");
        }
    }
}
