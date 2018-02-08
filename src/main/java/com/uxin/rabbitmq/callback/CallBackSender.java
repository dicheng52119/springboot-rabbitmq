package com.uxin.rabbitmq.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @program: springboot-rabbitmq
 * @description: 演示了具有回调信息的生产者配置
 * @author: DI CHENG
 * @create: 2018-01-23 15:28
 **/
@Component
public class CallBackSender implements RabbitTemplate.ConfirmCallback {

    private static final Logger logger = LoggerFactory.getLogger(CallBackSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplatenew;

    public void sendMessage(String msg) {

        logger.info("发送的消息内容：" + msg);
        rabbitTemplatenew.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(msg);
        this.rabbitTemplatenew.convertAndSend("exchange", "topic.messages", msg, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if(ack) {
            //消息发送成功
            logger.info("消息已送达，消息内容："+correlationData.getId());
        }else {
            //消息未发送成功 进行重发
            logger.info("消息未送达,未送达原因分析："+cause);
            //重发
            sendMessage(correlationData.getId());
        }

    }
}
