package com.example.chat_websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ 이 경로가 프론트에서 SockJS 연결하는 URL과 일치해야 함
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");       // 클라이언트가 구독할 경로 prefix
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 보낼 때 prefix
    }
}
