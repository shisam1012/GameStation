package com.example.server.Connect4Sockets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

public class RoomsC4 {
    private static final Map<String, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    public static void addToRoom(String roomCode, WebSocketSession session) {
        rooms.computeIfAbsent(roomCode, k -> new ArrayList<>()).add(session);
    }

    public static List<WebSocketSession> getRoom(String roomCode) {
        return rooms.get(roomCode);
    }
}
