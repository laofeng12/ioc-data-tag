package com.openjava.datatag.component;

import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * redis消息处理器接口
 * @author zmk
 */
@Component
public interface  RedisMsg {
    void receiveMessage(String message) throws Exception;
}
