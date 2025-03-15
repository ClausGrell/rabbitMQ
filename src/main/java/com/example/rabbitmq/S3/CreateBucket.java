package com.example.rabbitmq.S3;

import com.rabbitmq.client.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

public class CreateBucket {


    String s3url = "http://192.168.0.184:9000";
    String region = "us-west-2";
    String accesskeyid = "PQZEgUSS3wMMtSubdBo6";
    String secretaccesskey = "B8OXpqAo3g31KY3XcTQBk3R6oap2lUTbCh5jFzMK";

    public static void main( String args[]) {
        CreateBucket createBucket = new CreateBucket();
        createBucket.createBucket();
    }

    private void createBucket() {
        S3Client s3Client = getS3Client();
        Connection rabbitConnection = getRabbitConnection();

        try {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket("test1").build();
            var a = s3Client.deleteBucket(deleteBucketRequest);
        } catch (Exception e) {
            System.out.println("No bucket to delete");
        }

        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket("test1").build();
        var b = s3Client.createBucket(createBucketRequest);
        System.out.println("Bucket created");

        Channel channel = null;
        try {
            channel = rabbitConnection.createChannel();
            channel.exchangeDeclare("AutoExchange", BuiltinExchangeType.TOPIC);
            channel.queueBind("testQ", "AutoExchange", "AutoKey");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("abgiNCA7PEXrRCgFgbL3", "A4qpwWoGojTG1gR99pdtZTP2tOQaeoHcunzXcaq9");
        SnsClient snsClient = SnsClient.builder()
                .endpointOverride(URI.create(s3url))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_WEST_2) // Replace with your desired region
                .build();
        // Create an SNS topic
        String topicName = "test1-topic";

        CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);
        String snsTopicArn = createTopicResponse.topicArn();

        System.out.println("SNS Topic ARN: " + snsTopicArn);




        try {
            rabbitConnection.close();
            s3Client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Done so far");
    }

    public S3Client getS3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accesskeyid, secretaccesskey);
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(s3url))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds)) // Credentials (use IAM, or set up profile)
                .region(Region.of(region))  // Replace with your region
                .build();
        return s3Client;
    }

    public  Connection getRabbitConnection() {

        // Create connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.184");
        factory.setPort(5672);

        // Optional: set up credentials
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            Connection connection = factory.newConnection();
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }



    private static void createBucket(S3Client s3Client, String bucketName) {
        try {
            // Create a bucket request
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Call the create bucket method
            s3Client.createBucket(createBucketRequest);

            System.out.println("Bucket created successfully: " + bucketName);

        } catch (S3Exception e) {
            System.err.println("Error creating bucket: " + e.awsErrorDetails().errorMessage());
        }
    }



}
