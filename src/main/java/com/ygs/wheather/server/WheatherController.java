package com.ygs.wheather.server;
import com.google.gson.Gson;
import com.ygs.wheather.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.lang.invoke.MethodHandles;
import java.security.Principal;

import static com.ygs.wheather.server.SocketConfig.*;
import static java.lang.Thread.sleep;

@Controller
public class WheatherController {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Gson gson = new Gson();

    public static final String ENDPOINT_REGISTER = "/register";

    @Autowired
    public WheatherController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
/*
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
     //
        //   Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
*/
    @MessageMapping("/topic/weather")
    public void getWeather(Message<Object>message,@Payload Request payload,Principal principal) throws  Exception{

        if(payload!=null) {
            String username = payload.getUsername();
            Respond respond = new Respond(username, WeatherInformer.userData.get(username).getWheather());
            log.info("Try to send some to username " + username + "wheather info" + respond.getWeather().getDaily().getData().get(0).getTemperatureHigh());
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

       // log.info(ip.getIP());
        if(!WeatherInformer.userData.containsKey(username)) {
            WeatherInformer.userData.put(username, new UserData(payload.getIP()));
        }
        log.info("new registration: username="+username+", payload="+payload);

        log.info("send to user");
        Weather weather= WeatherParser.getWeather(48.4322,37.3322);
        Respond respond= new Respond(username,weather);
        messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, respond);

        messagingTemplate.convertAndSend(SocketConfig.SUBSCRIBE_QUEUE,respond);


    }



}
