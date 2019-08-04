package com.ygs.wheather.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
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
    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableSimpleBroker("/topic","/user","/queue");
        config.enableSimpleBroker(SUBSCRIBE_QUEUE, SUBSCRIBE_USER_REPLY,"/topic");
        config.setUserDestinationPrefix(SUBSCRIBE_USER_PREFIX);
        //config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

     /*   registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*")
                .withSockJS();;
     */

        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();

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
        registry.addEndpoint(ENDPOINT_CONNECT, WheatherController.ENDPOINT_REGISTER).setHandshakeHandler(new Handler()).setAllowedOrigins("*");
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
    }
}