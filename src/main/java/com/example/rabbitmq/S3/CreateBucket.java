package com.example.rabbitmq.S3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
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

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

public class  CreateBucket {


    String s3url = "http://192.168.0.184:9000";
    String region = "us-west-2";
    String accesskeyid = "PQZEgUSS3wMMtSubdBo6";
    String secretaccesskey = "B8OXpqAo3g31KY3XcTQBk3R6oap2lUTbCh5jFzMK";

    public static void main( String args[]) {
        CreateBucket createBucket = new CreateBucket();
        createBucket.createBucket();
//        createBucket.addMetaData();
//        createBucket.getMetaData();
//        createBucket.listBuckets();
//        createBucket.listBucketObjects("mbs");
    }

    private void addMetaData() {
        S3Tagging s3Tagging = new S3Tagging(s3url,region, accesskeyid, secretaccesskey);
        s3Tagging.addMetaData();
    }


    private void getMetaData() {
        S3Tagging s3Tagging = new S3Tagging(s3url,region, accesskeyid, secretaccesskey);
        s3Tagging.getMetaData();
    }


    public void listBuckets() {
        S3Client s3Client = getS3Client();
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        try {
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
            System.out.println("List of S3 Buckets:");
            for (Bucket bucket : listBucketsResponse.buckets()) {
                System.out.println(bucket.name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always close the client
            s3Client.close();
        }
    }


    public void listBucketObjects(String bucketName) {
        S3Client s3Client = getS3Client();
        try  {
            ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);
            for (S3Object s3Object : response.contents()) {
                System.out.println("Object Key: " + s3Object.key());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    }



    private void createBucket() {
        S3Client s3Client = getS3Client();
        deleteBucket(s3Client, "test1");

        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket("test1").build();
        var b = s3Client.createBucket(createBucketRequest);
        System.out.println("Bucket created");

        createAndBindExchange("AutoExchange", "AutoKey", "testQ");


        SnsClient snsClient = getSnsClient();
        // Create an SNS topic
        String topicName = "test1-topic";
        CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        // Virker ikke i minio
        CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);
        String snsTopicArn = createTopicResponse.topicArn();
        System.out.println("SNS Topic ARN: " + snsTopicArn);
        s3Client.close();
        System.out.println("Done so far");
    }

    private void deleteBucket(S3Client s3Client, String bucketName) {
        try {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket("test1").build();
            var a = s3Client.deleteBucket(deleteBucketRequest);
        } catch (Exception e) {
            System.out.println("No bucket to delete");
        }

    }

    private void createAndBindExchange(String exchange, String topic, String queueName) {
        Connection rabbitConnection = getRabbitConnection();
        Channel channel = null;
        try {
            channel = rabbitConnection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
            channel.queueBind(queueName, exchange, topic);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            rabbitConnection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private SnsClient getSnsClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accesskeyid, secretaccesskey);
        SnsClient snsClient = SnsClient.builder()
                .endpointOverride(URI.create(s3url))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region)) // Replace with your desired region
                .build();
        return snsClient;
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
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.0.184");
        factory.setPort(5672);
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
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            System.out.println("Bucket created successfully: " + bucketName);

        } catch (S3Exception e) {
            System.err.println("Error creating bucket: " + e.awsErrorDetails().errorMessage());
        }
    }


    public String generateHtml(JsonNode rootNode) {
        StringBuilder html = new StringBuilder();

        // Basic HTML structure
        html.append("<html><head><title>JSON Data</title></head><body>");
        html.append("<h1>JSON Data</h1>");
        html.append("<table border='1'>");

        // Iterate over the JSON fields
        rootNode.fieldNames().forEachRemaining(fieldName -> {
            JsonNode fieldValue = rootNode.get(fieldName);
            html.append("<tr><td>").append(fieldName).append("</td><td>").append(fieldValue.asText()).append("</td></tr>");
        });

        // Close HTML tags
        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    }

}
