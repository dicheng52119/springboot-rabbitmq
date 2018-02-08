package com.uxin.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: springboot-rabbitmq
 * @description: rabbitmq生产者配置(实现方式二)
 * 用于配置交换机和队列对应关系
 * 新增消息队列应该按照如下步骤
 * 1、增加queue bean，参见queueXXXX方法
 * 2、增加queue和exchange的binding
 * @author: DI CHENG
 * @create: 2018-01-23 16:02
 **/
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class RabbitMqExchangeConfig {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqExchangeConfig.class);

    /**
     * @Author:chenhf
     * @Description: 主题型交换机
     * @Date:下午5:49 2017/10/23
     * @param
     * @return
     */
    @Bean
    TopicExchange contractTopicExchangeDurable(RabbitAdmin rabbitAdmin){
        TopicExchange contractTopicExchange = new TopicExchange(RabbitMqEnum.Exchange.CONTRACT_TOPIC.getCode());
        rabbitAdmin.declareExchange(contractTopicExchange);
        logger.debug("完成主题型交换机bean实例化");
        return contractTopicExchange;
    }
    /**
     * 直连型交换机
     */
    @Bean
    DirectExchange contractDirectExchange(RabbitAdmin rabbitAdmin) {
        DirectExchange contractDirectExchange = new DirectExchange(RabbitMqEnum.Exchange.CONTRACT_DIRECT.getCode());
        rabbitAdmin.declareExchange(contractDirectExchange);
        logger.debug("完成直连型交换机bean实例化");
        return contractDirectExchange;
    }

    //在此可以定义队列
    @Bean
    Queue queueTest(RabbitAdmin rabbitAdmin){
        Queue queue = new Queue(RabbitMqEnum.QueueName.TESTQUEUE.getCode(),true,false,false);
        rabbitAdmin.declareQueue(queue);
        logger.debug("测试队列实例化完成");
        return queue;
    }

    //topic 1
    @Bean
    Queue queueTopicTest1(RabbitAdmin rabbitAdmin){
        Queue queue = new Queue(RabbitMqEnum.QueueName.TOPICTEST1.getCode(),true,false,false);
        rabbitAdmin.declareQueue(queue);
        logger.debug("话题测试队列1实例化完成");
        return queue;
    }
    //topic 2
    @Bean
    Queue queueTopicTest2(RabbitAdmin rabbitAdmin){
        Queue queue = new Queue(RabbitMqEnum.QueueName.TOPICTEST2.getCode(),true,false,false);
        rabbitAdmin.declareQueue(queue);
        logger.debug("话题测试队列2实例化完成");
        return queue;
    }


    //在此处完成队列和交换机绑定
    @Bean
    Binding bindingQueueTest(Queue queueTest, DirectExchange exchange, RabbitAdmin rabbitAdmin){
        Binding binding = BindingBuilder.bind(queueTest).to(exchange).with(RabbitMqEnum.QueueEnum.TESTQUEUE.getCode());
        rabbitAdmin.declareBinding(binding);
        logger.debug("测试队列与直连型交换机绑定完成");
        return binding;
    }
    //topic binding1
    @Bean
    Binding bindingQueueTopicTest1(Queue queueTopicTest1,TopicExchange exchange,RabbitAdmin rabbitAdmin){
        Binding binding = BindingBuilder.bind(queueTopicTest1).to(exchange).with(RabbitMqEnum.QueueEnum.TESTTOPICQUEUE1.getCode());
        rabbitAdmin.declareBinding(binding);
        logger.debug("测试队列与话题交换机1绑定完成");
        return binding;
    }

    //topic binding2
    @Bean
    Binding bindingQueueTopicTest2(Queue queueTopicTest2,TopicExchange exchange,RabbitAdmin rabbitAdmin){
        Binding binding = BindingBuilder.bind(queueTopicTest2).to(exchange).with(RabbitMqEnum.QueueEnum.TESTTOPICQUEUE2.getCode());
        rabbitAdmin.declareBinding(binding);
        logger.debug("测试队列与话题交换机2绑定完成");
        return binding;
    }
}
