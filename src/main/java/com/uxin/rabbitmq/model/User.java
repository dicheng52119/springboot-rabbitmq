package com.uxin.rabbitmq.model;

import java.io.Serializable;

/**
 * @program: springboot-rabbitmq
 * @description:
 * @author: DI CHENG
 * @create: 2018-01-23 14:48
 **/
public class User implements Serializable {

    private String name;
    private String pass;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
}
