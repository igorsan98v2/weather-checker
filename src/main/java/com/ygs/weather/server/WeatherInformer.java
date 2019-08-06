package com.ygs.weather.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


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



        userData.forEach((username,data)->{
            Location location = data.getLocation();
            data.setWheather(WeatherParser.getWeather(location.getLat(),location.getLon()));

        });


    }

    // this will send a message to an endpoint on which a client can subscribe
    @Scheduled(fixedRate = 1000*60)
    public void trigger() {

        userData.forEach((username,data)->{


            Respond respond = new Respond(username,data.getWheather());
            template.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY,respond);
        });


    }

}