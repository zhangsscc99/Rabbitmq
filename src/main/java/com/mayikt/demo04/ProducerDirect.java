package com.mayikt.demo04;

import com.mayikt.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerDirect {

    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //  创建Connection
        Connection connection = RabbitMQConnection.getConnection();
        // 创建Channel
        Channel channel = connection.createChannel();
        // 通道关联交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        String msg = "每特教育6666";
        channel.basicPublish(EXCHANGE_NAME, "sms", null, msg.getBytes());
        channel.close();
        connection.close();
        //mq Connection
    }

}