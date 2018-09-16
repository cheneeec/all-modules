package com.earnest.video.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


public class MessagePushSocket extends AbstractWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("receive a message,content:" + message.getPayload()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        while (true){
            session.sendMessage(new TextMessage("hello:"+ LocalDateTime.now()));
            TimeUnit.SECONDS.sleep(1);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接被关闭了");
    }


}
