package com.xxxx.simple.recv;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * 简单模式队列-消息接收者new
 */
public class Recv02 {
    // 队列名称
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // 构建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");
        try {
            // 通过工厂创建连接
            Connection connection = factory.newConnection();
            // 获取通道
            Channel channel = connection.createChannel();
            // 指定队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 获取消息
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            // 监听队列
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
