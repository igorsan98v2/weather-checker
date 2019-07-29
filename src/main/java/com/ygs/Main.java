package com.ygs;

import com.ygs.wheather_service.Weather;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Main {
    public static void main(String []args){
        /*
        WeatherParser weatherParser= new WeatherParser();
        Map<String,String> queryParams = new HashMap<String, String>();
        queryParams.put("exclude","currently");
        queryParams.put("lang","ru");
        queryParams.put("units","si");
        //Weather weather = null;
        Weather weather= weatherParser.getWeather(48.4322,37.3322,queryParams);
        if(weather!=null){
            System.out.println("Температура: "+weather.getHourly().getData().get(0).getTemperature());
        }
        else {
            System.out.println("LOSER");
        }
        https://rafaelhz.github.io/testing-websockets/
        */

        //SpringApplication.run(Main.class, args);
        SpringApplication app = new SpringApplication(Main.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));
        app.run(args);
    }
}
