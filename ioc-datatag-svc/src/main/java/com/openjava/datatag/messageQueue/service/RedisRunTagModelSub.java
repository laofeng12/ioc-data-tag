package com.openjava.datatag.messageQueue.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.component.RedisMsg;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * redis消息处理器pojo
 * 立即运行消息队列
 * @author zmk
 */
@Service
public class RedisRunTagModelSub implements RedisMsg {
    Logger logger = LogManager.getLogger(getClass());
    @Value("${messageChannel.runNow}")
    private String runNow;
    @Resource
    private DtTaggingModelService dtTaggingModelService;

    /**
     * 接收消息的方法
     * @param message 订阅消息体
     */
    @Override
    public void receiveMessage(String message) throws Exception{
        //注意通道调用的方法名要和RedisConfig2的listenerAdapter的MessageListenerAdapter参数2相同
        if ("true".equals(runNow)){
            DtTaggingModel model = JSONObject.parseObject(message, DtTaggingModel.class);
            logger.info("立即执行，模型id："+model.getTaggingModelId());
            dtTaggingModelService.calculation(model);
        }
    }
}
