package com.wang.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class EmitLogTopic {

    //交换机的名称
    public static final  String exchange_name="topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel= RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
        Map<String,String> bindingKeyMap =new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox", "被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");
        for(Map.Entry<String,String> bindingKeyEntry:bindingKeyMap.entrySet()){
            String bindingKey =bindingKeyEntry.getKey();
            String message=bindingKeyEntry.getValue();
            channel.basicPublish(exchange_name,bindingKey,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }
    }
}
