package com.kyle1elyk.rabbitproject.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author stead
 */
public class TopicSend {
    private static final String EXCHANGE_NAME = "topic_example";
    
    public static void main(final String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        
        try(Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
            
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            
            String routingKey = "unknown.important";
            String message = "This is important!";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            
            
            routingKey = "unknown.unimportant";
            message = "This is not important!";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            
            
            routingKey = "key.unimportant";
            message = "This isn't important, but it is related to key!";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            
            
            routingKey = "key.important";
            message = "";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        }
        
        
        
        
    }
}
