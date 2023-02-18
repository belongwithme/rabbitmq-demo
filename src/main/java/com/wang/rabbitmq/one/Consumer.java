package com.wang.rabbitmq.one;

import com.rabbitmq.client.*;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 */
public class Consumer {
    //队列的名称
    public static final String QUEUE_NAME="hello,wang";

    //接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        RabbitMqUtils util =new RabbitMqUtils();
        Channel channel1 =util.getChannel();

        //声明 接受消息
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback =consumerTag ->{
            System.out.println("消息消费被中断");
        };
        /**
         * 消费者消费信息
         * 1.消费哪个队列。
         * 2.消费成功之后是否要自动应答。
         * 3.消费者未成功消费的回调
         * 4.消费者取录消费的回调
         */
        System.out.println("C1等待接收消息....");
        channel1.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
