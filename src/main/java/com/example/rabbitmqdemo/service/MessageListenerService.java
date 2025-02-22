
package com.example.rabbitmqdemo.service;

import com.example.rabbitmqdemo.AppConfig;
import com.example.rabbitmqdemo.config.RabbitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageListenerService {

    @Autowired
    AppConfig appConfig;



    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessageh(Message message) {
        try {
            String body = new String(message.getBody());

            // Access the headers
            MessageProperties messageProperties = message.getMessageProperties();
            Map<String, Object> headers = messageProperties.getHeaders();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode event = objectMapper.readTree(body);

            // Example: Extract some fields from the event message
            String eventType = event.get("event").asText();
            String bucketName = event.get("bucket").get("name").asText();
            String objectKey = event.get("object").get("key").asText();

            System.out.println("Received event: " + eventType);
            System.out.println("Bucket: " + bucketName);
            System.out.println("Object: " + objectKey);


            System.out.println("QQQ: " + headers.get("QQQ"));
            // Further processing can be done here
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  System.out.println("Received Message: " + message);
    }


}
