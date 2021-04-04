package com.kyle1elyk.rabbitproject.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author stead
 */
public class FanoutSend {
    private static final String EXCHANGE_NAME = "fanout_example";
    
    public static void main(final String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        
        try(Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
            
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            
            String routingKey = "";
            String message = "This is being fanned out!";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            
            
            routingKey = "";
            message = "";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            
        }
        
        
        
        
    }
}
