package com.ygs.wheather.server;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.lang.invoke.MethodHandles;
import java.security.Principal;

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

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
     //
        //   Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }


    @MessageMapping(ENDPOINT_REGISTER)
    public void register(Message<Object> message, @Payload String payload, Principal principal) throws Exception {
        String username = principal.getName();

        log.info("new registration: username="+username+", payload="+payload);

        messagingTemplate.convertAndSendToUser(username, SocketConfig.SUBSCRIBE_USER_REPLY, "Thanks for your registration!");
        messagingTemplate.convertAndSend(SocketConfig.SUBSCRIBE_QUEUE, "Someone just registered saying: "+payload);
        new Thread(new Runnable() {
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
    }



}
