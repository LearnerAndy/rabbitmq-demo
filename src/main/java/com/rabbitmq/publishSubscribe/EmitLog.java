package com.rabbitmq.publishSubscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {
    // 交换机名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");
        // 通过工厂创建连接
        try (Connection connection = factory.newConnection();
             // 获取通道
             Channel channel = connection.createChannel()) {
            // 绑定交换机 fanout：广播模式
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            //创建消息
            String message = args.length < 1 ? "info: Hello World!" :
                    String.join(" ", args);
            // 发送消息到交换机
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
