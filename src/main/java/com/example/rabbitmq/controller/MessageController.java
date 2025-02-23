package com.example.rabbitmq.controller;

import com.example.rabbitmq.config.AppConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbitmq.config.RabbitConfig;

@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    AppConfig appConfig;

    String testMessage = "{\n" +
            "  \"event\": \"s3:ObjectCreated:Put\",\n" +
            "  \"bucket\": {\n" +
            "    \"name\": \"example-bucket\"\n" +
            "  },\n" +
            "  \"object\": {\n" +
            "    \"key\": \"path/to/object/file.txt\",\n" +
            "    \"size\": 2048,\n" +
            "    \"etag\": \"d41d8cd98f00b204e9800998ecf8427e\",\n" +
            "    \"content-type\": \"text/plain\"\n" +
            "  },\n" +
            "  \"timestamp\": \"2025-02-22T12:00:00Z\",\n" +
            "  \"user_metadata\": {\n" +
            "    \"author\": \"Jane Doe\",\n" +
            "    \"tags\": [\"document\", \"file\"]\n" +
            "  },\n" +
            "  \"aws_region\": \"us-west-2\",\n" +
            "  \"event_source\": \"aws:s3\",\n" +
            "  \"event_source_arn\": \"arn:aws:s3:::example-bucket\",\n" +
            "  \"request_id\": \"abcd1234efgh5678\",\n" +
            "  \"source_ip_address\": \"192.168.1.100\"\n" +
            "}\n";

    @GetMapping("/send")
    public String sendMessage() {
        String message = testMessage;
        rabbitTemplate.convertAndSend(appConfig.getExchangeName(), appConfig.getRoutingKey(), message);
        return "Message sent: " + message;
    }
}
