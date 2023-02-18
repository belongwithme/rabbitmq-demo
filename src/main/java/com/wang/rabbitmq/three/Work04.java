package com.wang.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;
import com.wang.rabbitmq.util.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Work04 {
    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        System.out.println("C2等待接收消息处理时间较长");
        DeliverCallback deliverCallback=(consumerTag, message)->{
            //沉睡30S
            SleepUtils.sleep(30);
            System.out.println("接收到的消息:"+new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 1.消息的标记 tag（应答的是哪一个标记）
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //采用手动应答
        boolean autoAck =false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,(consumerTag->{
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        }));
    }
}
