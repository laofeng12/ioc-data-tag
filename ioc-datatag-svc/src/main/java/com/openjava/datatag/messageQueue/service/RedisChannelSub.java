package com.openjava.datatag.messageQueue.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.component.RedisMsg;
import com.openjava.datatag.component.WebsocketServer;
import com.openjava.datatag.demo.dto.TopDepartmentRespDTO;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.ljdp.common.spring.SpringContext;
import org.ljdp.common.spring.SpringContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * redis消息处理器pojo
 * @author zmk
 */
@Service
public class RedisChannelSub implements RedisMsg {
    /**
     *
     * @param message
     * @throws Exception
     */
    @Override
    public void receiveMessage(String message) throws Exception {
//      注意通道调用的方法名要和RedisConfig2的listenerAdapter的MessageListenerAdapter参数2相同
//      System.out.println("这是RedisChannelSub"+"-----"+message);
        WebsocketServer websocketServer= (WebsocketServer) SpringContextManager.getBean("websocketServer");
//        System.out.println(message);
        DtTaggingModel model = JSONObject.parseObject(message, DtTaggingModel.class);
        JSONObject result = new JSONObject();
        result.put("taggingModelId",model.getTaggingModelId());
        result.put("runState",model.getRunState());
        websocketServer.sendMessage(model.getCreateUser(),result.toString());

    }
}
