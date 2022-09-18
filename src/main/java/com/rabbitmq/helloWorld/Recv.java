package com.rabbitmq.helloWorld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * "Hello World" 接收
 */
public class Recv {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //打开一个连接和一个通道，并声明我们将要消费的队列。请注意，这与发送发布到的队列相匹配。
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");
        /**
         * 不要使用 try-with-resource 语句来自动关闭通道和连接
         * 因为我们希望进程在消费者异步侦听消息到达时保持活动状态。
         */
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //
        /**
         * 我们将使用额外的DeliverCallback接口来缓冲服务器推送给我们的消息。
         *
         * 接收端将告诉服务器从队列中传递消息给我们。由于它会异步向我们推送消息，
         * 因此我们以对象的形式提供一个回调，该对象将缓冲消息，直到我们准备好使用它们。
         * 这就是DeliverCallback子类所做的。
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
