package com.boot.web.socket.controller;

import com.boot.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint(value = "/websocket/{appNo}")
public class DemoSocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoSocketController.class);

    @OnOpen
    public void onOpen(@PathParam("appNo") String appNo, Session session) {
        LOGGER.info("[" + appNo + "]加入连接!");
        WebSocketUtil.addSession(appNo, session);
    }

    @OnClose
    public void onClose(@PathParam("appNo") String appNo, Session session) {
        LOGGER.info("[" + appNo + "]断开连接!");
        WebSocketUtil.remoteSession(appNo);
    }

    @OnMessage
    public void OnMessage(@PathParam("appNo") String appNo, String message) {
        String messageInfo = "服务器对[" + appNo + "]发送消息：" + message;
        LOGGER.info(messageInfo);
        Session session = WebSocketUtil.ONLINE_SESSION.get(appNo);
        if ("heart".equalsIgnoreCase(message)) {
            LOGGER.info("客户端向服务端发送心跳");
            // 向客户端发送心跳连接成功
            message = "success";
        }
        // 发送普通信息
        WebSocketUtil.sendMessage(session, message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.error(session.getId() + "异常:", throwable);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }


}

