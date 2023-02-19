package com.wang.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect02 {
    public static final String exchange_name="direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk",true,false,false,null);

        channel.queueBind("disk",exchange_name,"error");
        //接收消息
        DeliverCallback deliverCallback =(consumerTag, message)->{
            System.out.println("ReceiveLogs02控制台打印接收到的消息:"+
                    new String(message.getBody(),"UTF-8"));
        };

        //消费者取消消息时回调接口
        channel.basicConsume("disk",true,deliverCallback,consumerTag->{});
    }

}
