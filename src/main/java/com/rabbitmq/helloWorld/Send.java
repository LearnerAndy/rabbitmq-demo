package com.rabbitmq.helloWorld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * "Hello World" 发送
 */
public class Send {
    //设置类并命名队列：
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //创建到服务器的连接
        ConnectionFactory factory = new ConnectionFactory();
        //我们连接到本地机器上的 RabbitMQ 节点
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");
        //创建一个通道（通过获取连接对象创建）
        /**
         * 我们可以使用 try-with-resources 语句，
         * 因为Connection和Channel都实现了java.io.Closeable。
         * 这样我们就不需要在代码中显式地关闭它们
         */

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //声明一个队列供我们发送
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //创建消息
            String message = "Hello World!";
            //将消息发布到队列中
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
