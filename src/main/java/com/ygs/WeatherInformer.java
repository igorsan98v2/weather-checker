package com.ygs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.Date;

@EnableScheduling
public class WeatherInformer {

    @Autowired
    private SimpMessagingTemplate template;

    // this will send a message to an endpoint on which a client can subscribe
    @Scheduled(fixedRate = 1000)
    public void trigger() {
        System.out.println("");
        Greeting helloMassage = new Greeting("name"+new Date().toString());
        this.template.convertAndSend("/topic/greetings", helloMassage);
    }

}