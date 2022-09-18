package com.rabbitmq.publishSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs02 {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //当我们不向queueDeclare()提供参数时， 我们会创建一个具有生成名称的非持久、独占、自动删除队列：
        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        /*
        限制RabbitMQ只发不超过1条的消息给同一个消费者。
        当消息处理完毕后，有了反馈，才会进行第二次发送。
        */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        /*
            autoAck = true代表自动确认消息
            autoAck = false代表手动确认消息 防止服务器宕机消息丢失。
        */
        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> { });
    }
}
