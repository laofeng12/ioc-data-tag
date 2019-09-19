package com.openjava.datatag.messageQueue.service;

import com.openjava.datatag.component.RedisMsg;

/**
 * redis消息处理器pojo
 * @author zmk
 */
public class RedisPmpSub implements RedisMsg {

    /**
     * 接收消息的方法
     *
     * @param message 订阅消息体
     */
    @Override
    public void receiveMessage(String message) throws Exception{
        //注意通道调用的方法名要和RedisConfig2的listenerAdapter的MessageListenerAdapter参数2相同
        System.out.println(message);
    }
}
