package com.ygs.wheather.server;

import com.ygs.wheather.common.Location;
import com.ygs.wheather.common.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherInformer {
    public static Map<String,UserData> userData = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate template;
    @Scheduled(fixedRate = 1000*60*60)

    public void updateWheatherInfo(){

        Map<String,String> queryParams = new HashMap<String, String>();
        queryParams.put("exclude","currently");
        queryParams.put("lang","ru");
        queryParams.put("units","si");

        //Weather weather = null;
        Weather weather= WeatherParser.getWeather(48.4322,37.3322,queryParams);
        userData.forEach((username,data)->{
            Location location = data.getLocation();
            data.setWheather(WeatherParser.getWeather(location.getLat(),location.getLon()));
            System.out.println("username: " + username + " data : " + data.getIp());
        });
        if(weather!=null){
            System.out.println("Температура: "+weather.getHourly().getData().get(0).getTemperature());
        }
        else {
            System.out.println("LOSER");
        }
    }

    // this will send a message to an endpoint on which a client can subscribe
    @Scheduled(fixedRate = 10000)
    public void trigger() {
        System.out.println("I m trying to send some");
        Greeting helloMassage = new Greeting("name"+new Date().toString());
        userData.forEach((username,data)->{
          //  Location location = data.getLocation();
            //data.setWheather(WeatherParser.getWeather(location.getLat(),location.getLon()));
            System.out.println("username: " + username + " data : " + data.getIp());
            template.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY,"Температура"+data.getWheather().getHourly().getData().get(0).getTemperature());
        });
        //this.template.convertAndSendToUser("","",helloMassage);
        this.template.convertAndSend("/topic/greetings", helloMassage);

    }

}