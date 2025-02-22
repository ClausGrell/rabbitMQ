
package com.example.rabbitmq.service;

import com.example.rabbitmq.AppConfig;
import com.example.rabbitmq.config.RabbitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MessageListenerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerService.class);

    @Autowired
    AppConfig appConfig;

    private static final boolean REQUEUE = true;
    private static final boolean NOREQUEUE = false;
    private static final boolean SINGLEMESSAGE = false;
    private static final boolean MULTIBLEMESSAGES = true;


    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(Message message, Channel channel) {
        try {
            processMessage(message, channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Message message, Channel channel) throws  IOException {

        String body = new String(message.getBody());
        logger.info("body: " + body);

        // Access the headers
        MessageProperties messageProperties = message.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode event = objectMapper.readTree(body);
//      Map jsonMap = objectMapper.readValue(body, Map.class);

        String eventType = event.get("event").asText();
        String bucketName = event.get("bucket").get("name").asText();
        String objectKey = event.get("object").get("key").asText();

        logger.info("Received event: " + eventType);
        logger.info("Bucket: " + bucketName);
        logger.info("Object: " + objectKey);
        logger.info("QQQ: " + headers.get("QQQ"));

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        //channel.basicNack(deliveryTag, MULTIBLEMESSAGES, NOREQUEUE);
        //channel.basicReject(deliveryTag, REQUEUE); //Reject en besked og kun en, og "requeue" den.

        channel.basicAck(deliveryTag, false); // Acknowledges all messages up to the specified delivery tag if true
        logger.info("Message acknowledged");


    }


}


/*
{
  "event": "s3:ObjectCreated:Put",
  "bucket": {
    "name": "example-bucket"
  },
  "object": {
    "key": "path/to/object/file.txt",
    "size": 2048,
    "etag": "d41d8cd98f00b204e9800998ecf8427e",
    "content-type": "text/plain"
  },
  "timestamp": "2025-02-22T12:00:00Z",
  "user_metadata": {
    "author": "Jane Doe",
    "tags": ["document", "file"]
  },
  "aws_region": "us-west-2",
  "event_source": "aws:s3",
  "event_source_arn": "arn:aws:s3:::example-bucket",
  "request_id": "abcd1234efgh5678",
  "source_ip_address": "192.168.1.100"
}
*/

/*
{
  "event": "object_uploaded",
  "bucket": "example-bucket",
  "object_key": "path/to/object/file.txt",
  "object_size": 2048,
  "content_type": "text/plain",
  "timestamp": "2025-02-22T12:00:00Z",
  "etag": "d41d8cd98f00b204e9800998ecf8427e",
  "storage_class": "STANDARD",
  "user_metadata": {
    "author": "John Doe",
    "tags": ["documentation", "file"]
  },
  "originating_system": "ceph"
}
 */


