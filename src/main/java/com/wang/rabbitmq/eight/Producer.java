package com.wang.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列之 生产者代码
 */
public class Producer {
    //普通交换机的名称
    public static final String NORMAL_EXCHANGE="normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        //死信消息，设置TTL时间，单位是ms
        AMQP.BasicProperties properties=
                new AMQP.BasicProperties()
                        .builder().expiration("10000").build();

        for(int i=1;i<11;i++){
            String message="info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());
        }
    }
}
