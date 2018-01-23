package com.uxin.rabbitmq.controller;

import com.uxin.rabbitmq.callback.CallBackSender;
import com.uxin.rabbitmq.model.User;
import com.uxin.rabbitmq.sender.fanout.FanoutSender;
import com.uxin.rabbitmq.sender.hello.HelloSender1;
import com.uxin.rabbitmq.sender.hello.HelloSender2;
import com.uxin.rabbitmq.sender.topic.TopicSender;
import com.uxin.rabbitmq.sender.user.UserSender;
import com.uxin.rabbitmq.sender.util.RabbitMqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springboot-rabbitmq
 * @description:
 * @author: DI CHENG
 * @create: 2018-01-23 14:32
 **/
@RestController
@RequestMapping("/rabbit")
public class RabbitTestController {

    @Autowired
    private HelloSender1 helloSender1;

    @Autowired
    private HelloSender2 helloSender2;

    @Autowired
    private UserSender userSender;

    @Autowired
    private TopicSender topicSender;

    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private CallBackSender callBackSender;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    /**
     * 单生产者-单消费者
     */
    @PostMapping("/oneToOne")
    public void oneToOne() {
        helloSender1.send();
    }

    /**
     * 单生产者-多消费者
     */
    @PostMapping("/oneToMany")
    public void oneToMany() {
        for(int i=0;i<10;i++){
            helloSender1.send("hellomsg:"+i);
        }
    }

    /**
     * 多生产者-多消费者
     */
    @PostMapping("/manyToMany")
    public void manyToMany() {
        for(int i=0;i<10;i++){
            helloSender1.send("hellomsg:"+i);
            helloSender2.send("hellomsg:"+i);
        }
    }

    /**
     * 实体类传输测试
     */
    @PostMapping("/userTest")
    public void userTest() {
        userSender.send();
    }

    /**
     * topic exchange类型rabbitmq测试
     */
    @PostMapping("/topicTest")
    public void topicTest() {
        topicSender.send();
    }

    /**
     * fanout exchange类型rabbitmq测试
     */
    @PostMapping("/fanoutTest")
    public void fanoutTest() {
        fanoutSender.send();
    }

    @PostMapping("/callback")
    public void callbak() {
        callBackSender.send();
    }


    @PostMapping("/directAndTopicTest")
    public void directAndTopicTest() {
        User testUser = new User();
        testUser.setName("1");
        rabbitMqSender.sendRabbitmqDirect("TESTQUEUE1",testUser);
        rabbitMqSender.sendRabbitmqTopic("lazy.1.2",testUser);
        rabbitMqSender.sendRabbitmqTopic("lazy.TEST.2",testUser);
    }
}
