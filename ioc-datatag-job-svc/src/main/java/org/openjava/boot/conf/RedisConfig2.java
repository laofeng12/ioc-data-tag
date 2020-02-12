package org.openjava.boot.conf;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.component.RedisMsg;
import com.openjava.datatag.messageQueue.service.RedisChannelSub;
import com.openjava.datatag.messageQueue.service.RedisRunTagModelSub;
import org.ljdp.common.spring.SpringContext;
import org.ljdp.core.service.ServiceFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

/**
 * 简易redis消息订阅组件
 * @author 郑绵铠
 */
@Configuration
@EnableCaching
public class RedisConfig2 {

    @Resource
    private RedisRunTagModelSub redisRunTagModelSub;
    @Resource
    private RedisChannelSub redisChannelSub;

    /**
     * Redis消息监听器容器
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫pmp和channel 的通道，多通道

        container.addMessageListener(listenerAdapter2(redisRunTagModelSub), new PatternTopic(Constants.DT_REDIS_MESSAGE_QUEUE_CHANL2));
        container.addMessageListener(listenerAdapter(redisChannelSub), new PatternTopic(Constants.DT_REDIS_MESSAGE_QUEUE_CHANL));
        //这个container 可以添加多个 messageListener
        return container;
    }

    /**
     * 配置消息接收处理类
     * @param redisMsg  自定义消息接收类
     * @return
     */
    @Bean()
    @Scope("prototype")
    MessageListenerAdapter  listenerAdapter(RedisMsg redisMsg) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        return new MessageListenerAdapter(redisMsg, "receiveMessage");//注意2个通道调用的方法都要为receiveMessage
    }

    /**
     * 模型立即运行监听器
     * @param redisMsg
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter2(RedisMsg redisMsg){
        return new MessageListenerAdapter(redisMsg, "receiveMessage");
    }

}
