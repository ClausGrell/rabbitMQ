package com.example.rabbitmq.config;

import com.example.rabbitmq.service.RabbitListenerService;
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

    private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    @Autowired
    AppConfig appConfig;

    @Bean
    public Queue queue() {
        logger.info("appConfig.getQueueName()=" + appConfig.getQueueName());
        return new Queue(appConfig.getQueueName(), true); // durable queue
    }

    @Bean
    public TopicExchange exchange() {
        logger.info("appConfig.getExchangeName()=" + appConfig.getExchangeName());
        return new TopicExchange(appConfig.getExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        logger.info("Introduce local variable=" + appConfig.getRoutingKey());
        return BindingBuilder.bind(queue).to(exchange).with(appConfig.getRoutingKey());
    }
}
