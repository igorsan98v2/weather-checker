package com.ygs.weather.client;

import com.ygs.weather.server.StopMessage;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class StopMessageHandler  implements StompFrameHandler {
    private final Consumer<StopMessage> frameHandler;
    public StopMessageHandler(Consumer<StopMessage> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return StopMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        frameHandler.accept((StopMessage) payload);
    }
}
