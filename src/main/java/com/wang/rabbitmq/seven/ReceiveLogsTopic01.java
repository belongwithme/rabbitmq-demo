package com.wang.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 声明主题交换机 及相关队列
 *
 * 消费者C1
 */
public class ReceiveLogsTopic01 {

    //交换机的名称
    public static final String exchange_name="topic_logs";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
//        //声明交换机
//        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
        //声明队列
        String queueName="Q1";

        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchange_name,"*.orange.*");
        System.out.println("等待接收消息");
        DeliverCallback deliverCallback =(consumerTag,delivery)->{
            System.out.println(new String(delivery.getBody(),"UTF-8"));
            System.out.println("接收队列:"+queueName+ " 绑定键:"+delivery.getEnvelope().getRoutingKey());
        };




        //接收信息
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
    }
}
