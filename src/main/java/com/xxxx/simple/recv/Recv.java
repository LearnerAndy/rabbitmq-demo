package com.xxxx.simple.recv;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * 简单模式队列-消息接收者old
 */
public class Recv {
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
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 获取消息并在控制台打印
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            // 监听队列
            channel.basicConsume(QUEUE_NAME,true,consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
