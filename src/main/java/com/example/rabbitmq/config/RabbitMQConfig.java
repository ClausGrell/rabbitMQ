package com.example.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // Declare the queue, exchange, and binding
    public static final String QUEUE_NAME = "myQueue";
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY = "myRoutingKey";

    // ConnectionFactory configuration

    // Declare the Queue
    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    // Declare the Exchange
    @Bean
    public TopicExchange myExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Bind the queue to the exchange with the routing key

    // Configure the listener container
    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String body = new String(message.getBody());
                System.out.println("Received message: " + body);  // Handle your message here
            }
        });
        return container;
    }
}