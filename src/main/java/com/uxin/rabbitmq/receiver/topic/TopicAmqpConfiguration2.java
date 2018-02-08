package com.uxin.rabbitmq.receiver.topic;

import com.rabbitmq.client.Channel;
import com.uxin.rabbitmq.config.RabbitMqConfig;
import com.uxin.rabbitmq.model.User;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

/**
 * @program: springboot-rabbitmq
 * @description:topic消息确认配置 (实现方式二)
 * springboot注解方式监听队列，无法手动指定回调，所以采用了实现ChannelAwareMessageListener接口，
 * 重写onMessage来进行手动回调，详见以下代码,详细介绍可以在spring的官网上找amqp相关章节阅读
 * @author: DI CHENG
 * @create: 2018-01-23 16:38
 **/
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class TopicAmqpConfiguration2 {

    @Bean("topicTest2Container")
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("TOPICTEST2");
        container.setMessageListener(exampleListener2());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }


    @Bean("topicTest2Listener")
    public ChannelAwareMessageListener exampleListener2(){
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                User testUser = (User) SerializationUtils.deserialize(message.getBody());
                System.out.println("TOPICTEST2："+testUser.toString());
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

            }
        };
    }

}
