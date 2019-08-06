package com.ygs.weather.server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.security.Principal;

import static com.ygs.weather.server.SocketConfig.*;


@Controller
public class WheatherController {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    public static final String ENDPOINT_REGISTER = "/register";
    /*
    @Autowired
    public WheatherController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

     */

    @MessageMapping("/topic/weather")
    public void getWeather(Message<Object>message,@Payload Request payload,Principal principal) throws  Exception{

        if(payload!=null) {
            String username = payload.getUsername();
            Respond respond = new Respond(username, WeatherInformer.userData.get(username).getWheather());
            log.info("Message about weather for " + username + "weather info");
            messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, respond);
        }
    }

    @MessageMapping("/topic/ping")
    public void getPong(Message<Object>message, @Payload PingPong ping, Principal principal) throws Exception{
            if(ping!=null){
                ping.setPong();
                String username = ping.getUsername();
                log.info("ping from user "+username+" to server"+ping.calcDelay());
                messagingTemplate.convertAndSendToUser(username,PING_REPLY,ping);
            }
    }

    @MessageMapping(ENDPOINT_REGISTER)
    public void register(Message<Object> message, @Payload IP  payload, Principal principal) throws Exception {
        String username = principal.getName();


        if(!WeatherInformer.userData.containsKey(username)) {
            WeatherInformer.userData.put(username, new UserData(payload.getIP()));
        }
        log.info("new registration: username="+username+", payload="+payload);

        Respond respond= new Respond(username,null);
        messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, respond);
        messagingTemplate.convertAndSend(SocketConfig.SUBSCRIBE_QUEUE,respond);


    }



}
