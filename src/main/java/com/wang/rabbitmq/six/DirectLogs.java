package com.wang.rabbitmq.six;

import com.rabbitmq.client.Channel;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class DirectLogs {
    public static final String exchange_name="direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel= RabbitMqUtils.getChannel();


        Scanner scanner=new Scanner(System.in);

        while(scanner.hasNext()){
            String message =scanner.next();
            channel.basicPublish(exchange_name,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
