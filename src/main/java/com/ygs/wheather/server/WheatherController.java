package com.ygs.wheather.server;
import com.google.gson.Gson;
import com.ygs.wheather.common.IP;
import com.ygs.wheather.common.Request;
import com.ygs.wheather.common.Respond;
import com.ygs.wheather.common.Weather;
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

import static com.ygs.wheather.server.SocketConfig.SUBSCRIBE_USER_PREFIX;
import static com.ygs.wheather.server.SocketConfig.SUBSCRIBE_USER_REPLY;
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
    @MessageMapping(SUBSCRIBE_USER_PREFIX +SUBSCRIBE_USER_REPLY)
    @SendToUser("SUBSCRIBE_USER_REPLY")
    public Weather getWeather(Message<Object>message,@Payload String payload,Principal principal) throws  Exception{
        log.info(principal.getName());
        return null;
    }

    @MessageMapping(ENDPOINT_REGISTER)
    public void register(Message<Object> message, @Payload IP  payload, Principal principal) throws Exception {
        String username = principal.getName();
        //IP ip = (IP)message;
       // log.info(ip.getIP());
        if(!WeatherInformer.userData.containsKey(username)) {
            WeatherInformer.userData.put(username, new UserData(payload.getIP()));
        }
        log.info("new registration: username="+username+", payload="+payload);
        IP pak = new IP("Fuck I got it");
        log.info("send to user");
        Respond respond= new Respond(username,null);
        messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, respond);
        //pak.setIP(payload.toString());
       //  messagingTemplate.convertAndSend(SocketConfig.SUBSCRIBE_QUEUE,pak);

        //messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, "Thanks for your registration!");
        //messagingTemplate.convertAndSend(SocketConfig.SUBSCRIBE_QUEUE, "Someone just registered saying: "+payload);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                while (true){

                    messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, "Thanks for your registration!"+username);
                    log.info("resended to username:"+username);
                    Thread.currentThread().sleep(5000);
                }
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();

         */
    }



}
