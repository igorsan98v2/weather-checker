package com.ygs.weather.client;

import com.ygs.weather.server.PingPong;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class PingPongFrameHandler  implements StompFrameHandler {
    private final Consumer<PingPong> frameHandler;

    public PingPongFrameHandler(Consumer<PingPong> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return PingPong.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        frameHandler.accept((PingPong) payload);
    }
}
