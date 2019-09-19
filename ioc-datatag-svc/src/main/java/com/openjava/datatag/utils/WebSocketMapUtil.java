package com.openjava.datatag.utils;

import com.openjava.datatag.component.WebsocketServer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketMapUtil {
    public static ConcurrentMap<String, List<WebsocketServer>> webSocketMap = new ConcurrentHashMap<>();

    public static void put(String key, List<WebsocketServer> myWebSocketServerList){
        webSocketMap.put(key, myWebSocketServerList);
    }

    public static  List<WebsocketServer> get(String key){
        return webSocketMap.get(key);
    }

    public static void remove(String key){
        webSocketMap.remove(key);
    }

    public static Collection<List<WebsocketServer>> getValues(){
        return webSocketMap.values();
    }

    public static void removeBySessionId(String sessionId){
        for(ConcurrentMap.Entry<String,List<WebsocketServer>> e: webSocketMap.entrySet() ){
            List<WebsocketServer> socketList = e.getValue();
            for (int i = 0; i < socketList.size(); i++) {
                WebsocketServer w = socketList.get(i);
                if (w.getSession().getId().equals(sessionId)) {
                    socketList.remove(w);//删除旧的
                    if (socketList.size()<=0) {
                        webSocketMap.remove(e.getKey());
                    }else {
                        webSocketMap.put(e.getKey(),socketList);
                    }
                }
            }
        }
    }


}