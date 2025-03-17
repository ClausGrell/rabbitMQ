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
//        createBucket.createBucket();
//        createBucket.addMetaData();
        createBucket.getMetaData();
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

            String json = "{\"EventName\":\"s3:ObjectCreated:PutTagging\",\"Key\":\"test/58260c0e-c4b4-4686-9cfc-3f732ff7f6e1\",\"Records\":[{\"eventVersion\":\"2.0\",\"eventSource\":\"minio:s3\",\"awsRegion\":\"\",\"eventTime\":\"2025-03-16T16:02:04.633Z\",\"eventName\":\"s3:ObjectCreated:PutTagging\",\"userIdentity\":{\"principalId\":\"minioadmin\"},\"requestParameters\":{\"principalId\":\"minioadmin\",\"region\":\"\",\"sourceIPAddress\":\"10.0.2.100\"},\"responseElements\":{\"content-length\":\"0\",\"x-amz-id-2\":\"dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8\",\"x-amz-request-id\":\"182D53F0D14C3556\",\"x-minio-deployment-id\":\"76fc4486-4468-4863-a1e9-913f479c09d7\",\"x-minio-origin-endpoint\":\"http://10.0.2.100:9000\"},\"s3\":{\"s3SchemaVersion\":\"1.0\",\"configurationId\":\"Config\",\"bucket\":{\"name\":\"test\",\"ownerIdentity\":{\"principalId\":\"minioadmin\"},\"arn\":\"arn:aws:s3:::test\"},\"object\":{\"key\":\"58260c0e-c4b4-4686-9cfc-3f732ff7f6e1\",\"size\":10,\"eTag\":\"94b54a8627da3ce4b54dd168bf40d229\",\"contentType\":\"application/octet-stream\",\"userMetadata\":{\"content-disposition\":\"58260c0e-c4b4-4686-9cfc-3f732ff7f6e1\",\"content-type\":\"application/octet-stream\"},\"sequencer\":\"182D53F0D09E8471\"}},\"source\":{\"host\":\"10.0.2.100\",\"port\":\"\",\"userAgent\":\"NiFi, aws-sdk-java/1.12.710 Linux/6.5.0-44-generic OpenJDK_64-Bit_Server_VM/21.0.3+9-Ubuntu-1ubuntu123.10.1 java/21.0.3 kotlin/1.9.23 vendor/Ubuntu cfg/retry-mode/legacy cfg/auth-source#unknown\"}}]}";
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);

            // Convert the JSON to an HTML table
            String html = generateHtml(jsonNode);
            System.out.println("****************************************'");
            System.out.println(html);
            System.out.println("****************************************'");

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

            // List objects in the bucket
            ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            // Get the objects
            ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);

            // Print the object keys
            for (S3Object s3Object : response.contents()) {
                System.out.println("Object Key: " + s3Object.key());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }    }



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
