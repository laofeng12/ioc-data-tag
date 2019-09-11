package com.openjava.datatag.component.websocket;

/**
 * @author zmk
 */

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.utils.WebSocketMapUtil;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * api接口
 *
 * @author zmk
 */
@Component
@Data
@ServerEndpoint(value = "/datatag/websocket/server/{userId}")
public class WebsocketServer {
    private Logger logger = LogManager.getLogger(getClass());
    //客户端连接会话
    private Session session;

    /**
     * 连接建立成功调用的方法
     **/
    @OnOpen
    public void onOpen(@PathParam(value = "userId")String userId, Session session){
        this.session = session;
        List<WebsocketServer> myWebSocketList =  WebSocketMapUtil.get(userId);
        if (CollectionUtils.isEmpty(myWebSocketList)) {
            myWebSocketList = new ArrayList<>();
        }
        myWebSocketList.add(this);
        WebSocketMapUtil.put(userId,myWebSocketList);
    }


    /**
　　* 连接关闭后触发的方法
 　　*/
    @OnClose
    public void onClose(){
        //从map中删除
        WebSocketMapUtil.removeBySessionId(session.getId());
        logger.info("====== onClose:"+session.getId()+" ======");
    }


    /**
　　 * 接收到客户端消息时触发的方法
　　 */
    @OnMessage
    public void onMessage(String params,Session session) throws Exception{
        //获取服务端到客户端的通道
        List<WebsocketServer> myWebSocketList =  WebSocketMapUtil.get(params);
        WebsocketServer myWebSocket =null;
        for (WebsocketServer w:myWebSocketList) {
            if (w.getSession().getId().equals(session.getId())) {
                myWebSocket = w;
            }
        }
        logger.info("收到来自"+session.getId()+"的消息"+params);
        String result = "收到来自"+session.getId()+"的消息"+params;
        //返回消息给Web Socket客户端（浏览器）
        myWebSocket.sendMessage(Long.valueOf(params),1,"成功！",result);
    }

    /**
　　* 发生错误时触发的方法
　　*/
    @OnError
    public void onError(Session session,Throwable error){
        logger.info(session.getId()+"连接发生错误"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Long  userId,int status,String message,Object datas) throws IOException{
        JSONObject result = new JSONObject();
        List<WebsocketServer> websocketServerList =  WebSocketMapUtil.get(userId.toString());
        if (CollectionUtils.isNotEmpty(websocketServerList)){
            result.put("status", status);
            result.put("message", message);
            result.put("datas", datas);
            for (int i = 0; i < websocketServerList.size(); i++) {
                websocketServerList.get(i).getSession().getBasicRemote().sendText(result.toString());
            }
        }

    }


}
