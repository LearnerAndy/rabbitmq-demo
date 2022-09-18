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
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        //创建到服务器的连接
        ConnectionFactory factory = new ConnectionFactory();
        //我们连接到本地机器上的 RabbitMQ 节点
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            for (int i = 1; i <= 20; i++) {
                String message = "Hello World! ----- " + i;
                // 将产生的消息放入队列
                channel.basicPublish("", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }


        }
    }

}