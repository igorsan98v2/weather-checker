package com.ygs.wheather.client;

import com.ygs.wheather.common.PingPong;
import com.ygs.wheather.common.Respond;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class PingPongFrameHandler  implements StompFrameHandler {
    private final Consumer<PingPong> frameHandler;
    private Logger logger = LogManager.getLogger(FrameHandler.class);
    public PingPongFrameHandler(Consumer<PingPong> frameHandler) {
        this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return PingPong.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("some respond was gotted "+payload.toString());
        frameHandler.accept((PingPong) payload);
    }
}
