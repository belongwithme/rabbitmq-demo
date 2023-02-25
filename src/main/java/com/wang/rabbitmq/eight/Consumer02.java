package com.wang.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer02 {

    //死信队列的名称
    public static final String DEAD_QUEUE= "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();

        System.out.println("等待接收消息");
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("Consumer02接收的消息是："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag->{});


    }


}
