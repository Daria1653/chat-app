package com.chat.websocket;

import com.chat.model.Message;
import com.chat.service.DatabaseService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ServerEndpoint("/chat")
public class ChatEndpoint {
    private static Map<Long, Session> sessions = new ConcurrentHashMap<>();
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static Gson gson = new Gson();
    private static DatabaseService db = new DatabaseService();
    
    private Long userId;

    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(0);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        executor.submit(() -> handleMessage(message, session));
    }

    private void handleMessage(String message, Session session) {
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String type = json.get("type").getAsString();

            if ("auth".equals(type)) {
                userId = json.get("userId").getAsLong();
                sessions.put(userId, session);
            } else if ("message".equals(type)) {
                Long toUserId = json.get("toUserId").getAsLong();
                String content = json.get("content").getAsString();
                
                Message msg = new Message(userId, toUserId, content);
                db.saveMessage(msg);

                JsonObject response = new JsonObject();
                response.addProperty("type", "message");
                response.addProperty("fromUserId", userId);
                response.addProperty("content", content);
                String responseStr = gson.toJson(response);

                Session toSession = sessions.get(toUserId);
                if (toSession != null && toSession.isOpen()) {
                    toSession.getBasicRemote().sendText(responseStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (userId != null) {
            sessions.remove(userId);
        }
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
