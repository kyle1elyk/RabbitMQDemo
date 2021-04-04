package com.kyle1elyk.rabbitproject.fanout;


import com.kyle1elyk.rabbitproject.queue.Recv;
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
public class FanoutRecv {
    private static final String EXCHANGE_NAME = "fanout_example";
    
    public static void main(final String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        
        
        /* Fanout set up */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        
        final String queueName = channel.queueDeclare().getQueue();
        
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        
        /* End Fanout set up */
        
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
            System.out.printf(" [x] Received %s:%s\n", delivery.getEnvelope().getRoutingKey(), messageString);
            
            
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        
    }
}
