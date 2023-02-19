package com.wang.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class EmitLog {

    //交换机的名称
    public static final String exchange_name="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        //channel.exchangeDeclare(exchange_name,"fanout");在消费者那里已经声明了，所以这里就不需要再写了。

        Scanner scanner=new Scanner(System.in);

        while(scanner.hasNext()){
            String message =scanner.next();
            channel.basicPublish(exchange_name,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
