package com.ygs.wheather.server;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import java.security.Principal;
import java.util.Map;

public class Handler  extends DefaultHandshakeHandler {
    private static final String ATTR_PRINCIPAL = "__principal__";

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        final String name;
        if (!attributes.containsKey(ATTR_PRINCIPAL)) {
            name = generateRandomUsername();
            attributes.put(ATTR_PRINCIPAL,name);
        }
        else {
            name = (String)attributes.get(ATTR_PRINCIPAL);
        }

        WeatherInformer.userData.put(name,new UserData(request.getRemoteAddress().toString()));
        return new Principal() {
            @Override
            public String getName()
            {

                return name;
            }

        };

    }

    private String generateRandomUsername() {
        RandomStringGenerator randomStringGenerator =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        return randomStringGenerator.generate(32);
    }
}
