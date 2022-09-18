package com.xxxx.simple.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式队列——消息发送者
 */
public class Send {
    // 队列名称
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/admin");


        Connection connection = null;
        Channel channel = null;
        try {
            // 获取连接对象
            connection = factory.newConnection();
            // 获取通道
            channel = connection.createChannel();
            // 开启事务
            channel.txSelect();
            /**
             * 声明队列
             *  第一个参数queue： 队列名称
             *  第二个参数durable： 是否持久化
             *  第三个参数Exclusive： 排他队列， 如果一个队列被声明为排他队列，该队列仅对首次声明它的连接可见， 并在连接断开时自动删除。
             *      这里需要注意三点：
             *          1. 排他队列是基于连接可见的， 同一连接的不同通道是可以同时访问同一个连接创建的排他队列的。
             *          2. "首次"， 如果一个连接已经声明了一个排他队列， 其他连接是不允许建立同名的排他队列的， 这个与普通队列不同。
             *          3. 即使该队列是持久化的， 一旦连接关闭或者客户端退出，该排他队列都会被自动删除的。
             *          这种队列适用于只限于一个客户端发送读取消息的应用场景。
             *  第四个参数Auto-delete： 自动删除， 如果该队列没有任何订阅的消费者的话， 该队列会被自动删除。
             *                          这种队列适用于临时队列。
             */
            //创建队列
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            // 创建消息
            String message = "hello rabbitmq";
            // 将产生的消息放入队列
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
            // 提交事务
            channel.txCommit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 关闭通道
            try {
                if (null != channel && channel.isOpen()){
                    channel.close();
                }
                if (null != connection&& connection.isOpen())   {
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }
}
