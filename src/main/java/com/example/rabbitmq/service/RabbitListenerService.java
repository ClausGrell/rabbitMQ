
package com.example.rabbitmq.service;

import com.example.rabbitmq.config.AppConfig;
import com.example.rabbitmq.S3.S3Tagging;

import com.example.rabbitmq.config.S3Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RabbitListenerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerService.class);
    private static final String QUEUE_NAME = "testQ";

    @Autowired
    S3Config s3Config;

    @Autowired
    AppConfig appConfig;

    private static final boolean REQUEUE = true;
    private static final boolean NOREQUEUE = false;
    private static final boolean SINGLEMESSAGE = false;
    private static final boolean MULTIBLEMESSAGES = true;


    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(Message message, Channel channel) {
        try {
            processMessage(message, channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Message message, Channel channel) throws  IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        String body = new String(message.getBody());
        logger.info("body: " + body);

        ObjectMapper objectMapper = new ObjectMapper();
        var jsonMap = objectMapper.readValue(body, Map.class);
        var eventRecords = (ArrayList) jsonMap.get("Records");
        var eventRecord = (Map) eventRecords.get(0);

        var eventName = eventRecord.get("eventName").toString();
        var bucketName = ((Map) ((Map) eventRecord.get("s3")).get("bucket")).get("name").toString();
        var objectKey = ((Map) ((Map) eventRecord.get("s3")).get("object")).get("key").toString();
        var deliveryTag = message.getMessageProperties().getDeliveryTag();

        logger.info("EventName = " + eventName);
        logger.info("bucketName = " + bucketName);
        logger.info("objectKey = " + objectKey);
        //logger.info("QQQ: " + headers.get("QQQ"));

        String[] parts = eventName.split(":");
        String eventType = parts[1];
        String eventSubtype = parts[2];

        S3Tagging s3Tagging = new S3Tagging(s3Config.getS3url(), s3Config.getRegion(), s3Config.getAccesskeyid(), s3Config.getSecretaccesskey());
        var tags = s3Tagging.getObjectTags(bucketName, objectKey);
        boolean objectComplient = s3Tagging.isComplient(tags);

        if (eventType.equals("ObjectCreated") && eventSubtype.equals("Put")) {
            // Check Compliance
//            S3Tagging s3Tagging = new S3Tagging(s3Config.getS3url(), s3Config.getRegion(), s3Config.getAccesskeyid(), s3Config.getSecretaccesskey());
            s3Tagging.doSomeTagging(bucketName, objectKey, "Complient", "true");
  //          var tags = s3Tagging.getObjectTags(bucketName, objectKey);      // Replace with your object key (the file name in the S3 bucket)
            logger.info("Message tagged");
            channel.basicAck(deliveryTag, false); // Acknowledges all messages up to the specified delivery tag if true
            //channel.basicNack(deliveryTag, MULTIBLEMESSAGES, NOREQUEUE);
            //channel.basicReject(deliveryTag, REQUEUE); //Reject en besked og kun en, og "requeue" den.
            logger.info("Message acknowledged");
        } else if (eventType.equals("ObjectCreated") && eventSubtype.equals("PutTagging")) {
            TimeValidator t = new TimeValidator(tags);
            var timeValidated = t.validate();
            if (timeValidated && !objectComplient) {
                s3Tagging.doSomeTagging(bucketName, objectKey, "Complient", "true");
            } else if (!timeValidated && objectComplient) {
                s3Tagging.doSomeTagging(bucketName, objectKey, "Complient", "false");
            }
            channel.basicAck(deliveryTag, false);
            logger.info("Unused message acknowledged");
        } else {
            channel.basicAck(deliveryTag, false);
            logger.info("Unused message acknowledged");
        }
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


