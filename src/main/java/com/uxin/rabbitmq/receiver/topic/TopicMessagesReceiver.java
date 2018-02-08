package com.uxin.rabbitmq.receiver.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: springboot-rabbitmq
 * @description: 使用注解监听队列，无法指定回调内容 (实现方式一)
 * @author: DI CHENG
 * @create: 2018-01-23 14:59
 **/
@Component
@RabbitListener(queues = "topic.messages")
public class TopicMessagesReceiver {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("topicMessagesReceiver  : " +msg);
    }
}
