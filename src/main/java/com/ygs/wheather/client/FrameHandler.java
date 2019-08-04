package com.ygs.wheather.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class FrameHandler implements StompFrameHandler {
    private final Consumer<String> frameHandler;
    private Logger logger = LogManager.getLogger(FrameHandler.class);
    public FrameHandler(Consumer<String> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
       // logger.info(payload.toString());
        frameHandler.accept(payload.toString());
    }
}