package com.wang.rabbitmq.one;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME="hello,wang";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
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
        /**
         * 生成一个队列
         * 1.队列名称。
         * 2.队列消息持久化（磁盘），默认存储在内容中。
         * 3.该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费。
         * 4.是否自动删除 最后一个消费者端开连接以后，该队列是否自动删除。
         * 5.其他参数
         */
         channel1.queueDeclare(QUEUE_NAME,false,false,false,null);
         //发消息
        String message="hello world";

        /**
         * 信道发消息，发送一个消费
         * 1.发送到哪个交换机
         * 2.路由的key值是哪个 本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel1.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");


    }
}
