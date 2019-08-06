package com.ygs.weather.client;

import com.ygs.weather.server.common.Respond;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class FrameHandler implements StompFrameHandler {
    private final Consumer<Respond> frameHandler;
    public FrameHandler(Consumer<Respond> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Respond.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        frameHandler.accept((Respond) payload);
    }

}