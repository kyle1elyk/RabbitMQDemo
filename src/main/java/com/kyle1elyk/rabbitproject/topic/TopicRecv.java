package com.kyle1elyk.rabbitproject.topic;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stead
 */
public class TopicRecv {
    private static final String EXCHANGE_NAME = "topic_example";
    
    public static void main(final String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        
        
        /* Topic set up */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        
        final String queueName = channel.queueDeclare().getQueue();
        
        channel.queueBind(queueName, EXCHANGE_NAME, "key.*");
        channel.queueBind(queueName, EXCHANGE_NAME, "*.important");
        
        /* End Topic set up */
        
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
                    Logger.getLogger(TopicRecv.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return;
            }
            
            String messageString = new String(messageBytes, "UTF-8");
            System.out.printf(" [x] Received %s:%s\n", delivery.getEnvelope().getRoutingKey(), messageString);
            
            
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        
    }
}
