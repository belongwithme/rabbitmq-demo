package com.wang.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程
 */
public class Work01 {
    //队列名称
    public static final String QUEUE_NAME ="hello,wang";
    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();

        //消息的接收
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println("接收到的消息:"+new String(message.getBody()));
        };

        //消息接收被取消时，执行下面的内容
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息者取消消费接口回调逻辑");
        };
        System.out.println("C1等待接收消息....");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
