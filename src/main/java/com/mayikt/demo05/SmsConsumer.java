package com.mayikt.demo05;

import com.mayikt.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SmsConsumer {
    /**
     * 定义短信队列
     */
    private static final String QUEUE_NAME = "direct_sms_queue";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("短信消费者...");
        // 创建我们的连接
        Connection connection = RabbitMQConnection.getConnection();
        // 创建我们通道
        final Channel channel = connection.createChannel();
        // 关联队列消费者关联队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "sms");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("短信消费者获取消息:" + msg);
            }
        };
        // 开始监听消息 自动签收
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }
}
/*
是不是只要写上了async的方法 在代码里调用 它的调用不会影响到调用之后的代码继续执行
ChatGPT
是的，一旦你在一个方法上标注了@Async注解，并且配置了相应的任务执行器（如这里使用的"taskExecutor"），那么这个方法就会异步执行。也就是说，当你在代码中调用这个方法时，调用者不会等待这个方法完成，而是立即继续执行方法调用之后的代码。

在你提供的代码片段中，sms()方法上标注了@Async("taskExecutor")，这意味着当你调用sms()时，它将在由“taskExecutor”管理的一个线程上异步执行。因此，调用sms()方法的代码不会被阻塞，它之后的代码会继续执行，不必等待sms()方法完成。这在处理需要长时间运行而又不希望阻塞当前线程的操作时非常有用，比如发送短信、执行复杂计算或者调用远程服务等。

要注意的是，使用@Async时，你还需要确保你的Spring应用上下文中配置了异步任务执行器，否则你可能会遇到NoSuchBeanDefinitionException的错误，因为Spring找不到名为"taskExecutor"的bean。
 */