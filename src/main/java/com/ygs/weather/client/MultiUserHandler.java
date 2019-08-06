package com.ygs.weather.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.invoke.MethodHandles;

public class MultiUserHandler extends StompSessionHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        try {
            String username = connectedHeaders.get("user-name").iterator().next();

        }
        catch (NullPointerException e){
            log.warn(e.getMessage());
        }

        super.afterConnected(session, connectedHeaders);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("handleException: exception="+exception);
        super.handleException(session, command, headers, payload, exception);
    }
}
