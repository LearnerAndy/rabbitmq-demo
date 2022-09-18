package com.rabbitmq.workQueues.autoAckDurability;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 工作队列——发送
 * 我们将稍微修改前面示例中的Send.java代码，以允许从命令行发送任意消息。
 * 该程序会将任务安排到我们的工作队列中，因此我们将其命名为 NewTask.java：
 */
public class NewTask {
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
            boolean durable = true;
            channel.queueDeclare("task_queue", durable, false, false, null);
            /**
             * 我们将稍微修改前面示例中的Send.java代码，以允许从命令行发送任意消息。
             */
            //创建消息
            String message = String.join(" " , args);
//            String message = String.join(" " , "NewTask", "First message.");
//            String message = String.join(" " , "NewTask", "Second message..");
//            String message = String.join(" " , "NewTask", "Third message...");
//            String message = String.join(" " , "NewTask", "Fourth message....");
//            String message = String.join(" " , "NewTask", "Fifth message.....");
            //将消息发布到队列中
            channel.basicPublish("", "task_queue",
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
