package com.kyle1elyk.rabbitproject.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Recv {

    private final static String QUEUE_NAME = "basic_publish_example";

    public static void main(final String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            
            byte[] messageBytes = delivery.getBody();
            System.out.printf("Consumer Tag: %s\nBody Length:%d\n", consumerTag, messageBytes.length);
            
            if (messageBytes.length == 0) {
                System.out.printf(" [x] Received nothing, closing");
                try {
                    
                    channel.close();
                    connection.close();
                } catch (IOException | TimeoutException ex) {
                    Logger.getLogger(Recv.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return;
            }
            
            String messageString = new String(messageBytes, "UTF-8");
            System.out.println(" [x] Received '" + messageString + "'");
            
            
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
