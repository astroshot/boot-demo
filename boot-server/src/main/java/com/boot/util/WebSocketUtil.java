package com.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class WebSocketUtil {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtil.class);

    public static final Map<String, Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    public static void addSession(String userKey, Session session) {
        ONLINE_SESSION.put(userKey, session);
    }

    public static void remoteSession(String userKey) {
        ONLINE_SESSION.remove(userKey);
    }

    public static Boolean sendMessage(Session session, String message) {
        if (session == null) {
            return false;
        }
        // getAsyncRemote()和getBasicRemote()异步与同步
        RemoteEndpoint.Async async = session.getAsyncRemote();
        //发送消息
        Future<Void> future = async.sendText(message);
        boolean done = future.isDone();
        logger.info("服务器发送消息给客户端" + session.getId() + "的消息:" + message + "，状态为:" + done);
        return done;
    }
}

