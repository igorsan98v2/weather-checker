package com.ygs;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GritingController {
    @MessageMapping("/hello")
    @Scheduled(fixedRate = 100)
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
     //
        //   Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
