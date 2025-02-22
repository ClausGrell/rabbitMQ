package com.example.rabbitmqdemo.config;

import com.example.rabbitmqdemo.AppConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Autowired
    AppConfig appConfig;
    public static final String QUEUE_NAME = "testQ";
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY = "myRoutingKey";

    @Bean
    public Queue queue() {
        var a = appConfig.getQueueName();
        System.out.println("a=" + a);
        return new Queue(QUEUE_NAME, true); // durable queue
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
