
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
            System.out.println("body: " + body);

            // Access the headers
            MessageProperties messageProperties = message.getMessageProperties();
            Map<String, Object> headers = messageProperties.getHeaders();

            ObjectMapper objectMapper = new ObjectMapper();
//            Map jsonMap = objectMapper.readValue(body, Map.class);

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


/*{
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
