package com.ygs.weather.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.lang.invoke.MethodHandles;
import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker

public class SocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String ENDPOINT_CONNECT = "/connect";
    public static final String SUBSCRIBE_USER_PREFIX = "/private";
    public static final String SUBSCRIBE_USER_REPLY = "/reply";
    public static final String SUBSCRIBE_QUEUE = "/queue";
    public static final String PING_REPLY = SUBSCRIBE_USER_REPLY+"/ping";
    public static final String SERVER_STOP_TOPIC = "/topic/stop";
    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableSimpleBroker("/topic","/user","/queue");
        config.enableSimpleBroker(SUBSCRIBE_QUEUE, SUBSCRIBE_USER_REPLY,PING_REPLY,"/topic");
        config.setUserDestinationPrefix(SUBSCRIBE_USER_PREFIX);

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {


        registry.addEndpoint("/gs-guide-websocket").setHandshakeHandler(new DefaultHandshakeHandler() {

            public boolean beforeHandshake(
                    ServerHttpRequest request,
                    ServerHttpResponse response,
                    WebSocketHandler wsHandler,
                    Map attributes) throws Exception {

                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest
                            = (ServletServerHttpRequest) request;
                    HttpSession session = servletRequest
                            .getServletRequest().getSession();
                    attributes.put("sessionId", session.getId());

                }
                return true;
            }})
                .setAllowedOrigins("*");
        registry.addEndpoint(ENDPOINT_CONNECT, WheatherController.ENDPOINT_REGISTER,SERVER_STOP_TOPIC).setHandshakeHandler(new Handler()).setAllowedOrigins("*");
    }


    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        log.info("<==> handleSubscribeEvent: username="+event.getUser().getName()+", event="+event);
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        log.info("===> handleConnectEvent: username="+event.getUser().getName()+", event="+event);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        log.info("<=== handleDisconnectEvent: username="+event.getUser().getName()+", event="+event);
        if(WeatherInformer.userData.containsKey(event.getUser().getName())){
            //delete user from auto-reply queue
            WeatherInformer.userData.remove(event.getUser().getName());
        }
    }
    @EventListener
    public void handleContentCloseEvent(ContextClosedEvent event){
        messagingTemplate.convertAndSend(SERVER_STOP_TOPIC,new StopMessage("\nTHE SERVER GOING DOWN ! THIS IS LAST MESSAGE FROM THE SERVER\n"));
        log.info("x==x\tapp go down");
    }
    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        container.setMaxBinaryMessageBufferSize(32768);

        log.info("Websocket factory returned");
        return container;
    }
}