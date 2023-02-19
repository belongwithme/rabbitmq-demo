package com.wang.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时不丢失的，放回队列中重新消费
 */
public class Task2 {
    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        //队列持久化
        boolean durable =true; //需要让Queue进行持久化
        //声明队列
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);
        //从控制台中输入信息
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            String message=scanner.next();
            channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
