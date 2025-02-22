package com.example.rabbitmqdemo.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbitmqdemo.config.RabbitConfig;

@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/send")
    public String sendMessage() {
        String message = "Hello, RabbitMQ!";
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, message);
        return "Message sent: " + message;
    }
}
