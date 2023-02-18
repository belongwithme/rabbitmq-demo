package com.wang.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtils {
    //得到一个连接的channel
    public static Channel getChannel() throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory =new ConnectionFactory();
        //工厂IP 连接RabbitMQ的队列
        factory.setHost("localhost");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("123");
        //创建连接
        Connection connection =factory.newConnection();
        //获取信道
        Channel channel1 =connection.createChannel();
        return  channel1;
    }
}
