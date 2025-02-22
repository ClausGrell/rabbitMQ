package com.example.rabbitmq.config;

import com.example.rabbitmq.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    private static final Logger logger = LoggerFactory.getLogger(com.example.rabbitmq.service.MessageListenerService.class);

    @Autowired
    AppConfig appConfig;

    public static final String QUEUE_NAME = "testQ";
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY = "myRoutingKey";

    @Bean
    public Queue queue() {
        var queueName = appConfig.getQueueName();
        logger.info("queueName=" + queueName);
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
