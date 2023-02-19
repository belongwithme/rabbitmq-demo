package com.wang.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息接收
 */
public class ReceiveLogs01 {

    //交换机的名称
    public static final String exchange_name ="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(exchange_name,"fanout");
        //声明一个队列 临时队列
        /**
         * 生成一个临时队列，队列的名称是随机的
         * 当消费者断开与队列的连接的时候，队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queueName,exchange_name,"");
        System.out.println("01等待接收消息，把接收到的消息打印在屏幕上.....");
        //接收消息
        DeliverCallback deliverCallback =(consumerTag,message)->{
            System.out.println("01控制台打印接收到的消息:"+new String(message.getBody(),"UTF-8"));
        };
        //消费者取消消息时的回调接口
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
    }
}
