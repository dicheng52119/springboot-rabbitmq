package com.uxin.rabbitmq.receiver.direct;

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
 * @description:Direct消费者配置
 * springboot注解方式监听队列，无法手动指定回调，所以采用了实现ChannelAwareMessageListener接口，
 * 重写onMessage来进行手动回调，详见以下代码,详细介绍可以在spring的官网上找amqp相关章节阅读
 * @author: DI CHENG
 * @create: 2018-01-23 16:24
 **/
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class ExampleAmqpConfiguration {

    @Bean("testQueueContainer")
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("TESTQUEUE");
        container.setMessageListener(exampleListener());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }

    @Bean("testQueueListener")
    public ChannelAwareMessageListener exampleListener() {
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                User testUser = (User) SerializationUtils.deserialize(message.getBody());
                //通过设置TestUser的name来测试回调，分别发两条消息，一条UserName为1，一条为2，查看控制台中队列中消息是否被消费
                if ("2".equals(testUser.getName())){
                    System.out.println(testUser.toString());
                    //第一个参数为接收到的tag，第二个参数用于响应是否接收成功
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }

                if ("1".equals(testUser.getName())){
                    System.out.println(testUser.toString());
                    //第一个参数为接收到的tag，第二个参数用于响应是否接收成功，第三个参数用于是否要求重发
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
                }

            }
        };
    }
}
