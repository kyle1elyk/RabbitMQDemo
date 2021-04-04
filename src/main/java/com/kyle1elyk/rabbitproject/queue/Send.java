package com.kyle1elyk.rabbitproject.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Queue;

import java.nio.charset.StandardCharsets;

public class Send {

    private final static String QUEUE_NAME = "basic_publish_example";

    public static void main(final String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            Queue.DeclareOk response = channel.queueDeclare(QUEUE_NAME, false, false, true, null);
            

            for (int i = 0; i < 11; i++) {
                String message = String.format("Message %d", i);

                if (i == 10) {
                    message = "";
                } else if (args.length > 0) {
                    message = args[0];
                }
                

                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                
                System.out.println(" [x] Sent '" + message + "'");
                
                Thread.sleep(1000);
            }
        }
    }
}
