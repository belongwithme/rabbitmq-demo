package com.wang.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.wang.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式
 *  使用时间 比较哪种确认方式是最好的
 *  1.单个确认
 *  2.批量确认
 *  3.异步批量确认
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int message_count=1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        //1.单个确认
//        ConfirmMessage.publishMessageIndividually();
        //2.批量确认
//        publishMessageBatch();
        //3.异步批量确认
        publishMessageAsync();

    }

    //单个确认
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel= RabbitMqUtils.getChannel();
        //队列的声明
        String queueName= UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始的时间
        long begin =System.currentTimeMillis();

        //批量发消息
        for (int i=0;i<message_count;i++){
            String message =i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            boolean flag =channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end =System.currentTimeMillis();

        System.out.println("发布"+message_count+"个单独确认消息，耗时"+(end-begin)+"ms");
    }

    //批量确认
    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel= RabbitMqUtils.getChannel();
        //队列的声明
        String queueName= UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始的时间
        long begin =System.currentTimeMillis();
        //批量确认消息大小
        int batchSize =100;
        //批量发消息 ,批量发布确认
        for (int i=0;i<message_count;i++){
            String message =i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //判断达到100条消息的时候，批量确认一次。
            if((i+1)%batchSize==0){
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end =System.currentTimeMillis();

        System.out.println("发布"+message_count+"个批量确认消息，耗时"+(end-begin)+"ms");
    }

    //异步发布确认
    public static void  publishMessageAsync() throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        //队列的声明
        String queueName= UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个hash表，适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目，只要给到序号
         * 3.支持高并发
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms=
                new ConcurrentSkipListMap<>();
        //消息确认成功 回调函数
        ConfirmCallback ackCallback =(deliveryTag,multiple)->{
           if(multiple){
               //2.删除掉已经确认的消息 剩下的就是未确认的消息
               ConcurrentNavigableMap<Long, String> confirmed =
                       outstandingConfirms.headMap(deliveryTag);
               confirmed.clear();
           }else{
               outstandingConfirms.remove(deliveryTag);
           }
            System.out.println("确认的消息:"+deliveryTag);
        };
        //消息确认失败 回调函数
        ConfirmCallback nackCallback=(deliveryTag,multiple)->{
            //3.打印一下未确认的消息都有哪些
            String message =outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息:"+message+":::"+"未确认的消息tag:"+deliveryTag);
        };
        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        /**
         * 1.监听哪些消息成功了
         * 2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback,nackCallback);//异步通知
        //开始的时间
        long begin =System.currentTimeMillis();
        //批量发消息 ,批量发布确认
        for (int i=0;i<message_count;i++){
            String message =i+"消息";
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.此处记录下所有要发送的消息 消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);

        }

        //结束时间
        long end =System.currentTimeMillis();
        System.out.println("发布"+message_count+"个异步发布确认消息，耗时"+(end-begin)+"ms");
    }


}
