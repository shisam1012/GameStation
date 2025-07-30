package com.example.server.Connect4Sockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket configuration class.
 * Registers the WebSocket handler for Connect4.
 */

@Configuration
@EnableWebSocket
public class WebSocketConfigC4 implements WebSocketConfigurer {

    private final Connect4WebSocketHandler handler;

    public WebSocketConfigC4(Connect4WebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Mapping the WebSocket endpoint
        registry.addHandler(handler, "/connect4-socket").setAllowedOrigins("*");
    }
}