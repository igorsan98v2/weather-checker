package com.ygs.wheather.client;

import com.ygs.wheather.common.IP;
import com.ygs.wheather.common.Respond;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class FrameHandler implements StompFrameHandler {
    private final Consumer<Respond> frameHandler;
    private Logger logger = LogManager.getLogger(FrameHandler.class);
    public FrameHandler(Consumer<Respond> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Respond.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
       // logger.info(payload.toString());
        frameHandler.accept((Respond) payload);
    }
}