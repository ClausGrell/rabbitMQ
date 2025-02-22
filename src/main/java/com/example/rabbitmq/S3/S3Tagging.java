package com.example.rabbitmq.S3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectTaggingResponse;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;

import java.net.URI;
import java.util.List;

public class S3Tagging {

    public void doSomeTagging() {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "vosCOcOnxKUmoDo5old7",    // Replace with your Access Key ID
                "PPqSIAYO9885FcwjTGW1mzqCVDBjoXXkjaGSSzE6" // Replace with your Secret Access Key
        );
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create("http://192.168.0.147:9000"))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds)) // Credentials (use IAM, or set up profile)
                .region(Region.of("us-west-2"))  // Replace with your region
                .build();

        // Define the tags you want to add
        List<Tag> tags = List.of(
                Tag.builder().key("Project").value("S3Example").build(),
                Tag.builder().key("Environment").value("Production").build()
        );

        // Create the tagging request
        Tagging tagging = Tagging.builder().tagSet(tags).build();
        PutObjectTaggingRequest putObjectTaggingRequest = PutObjectTaggingRequest.builder()
                .bucket("test-bucket")  // Replace with your bucket name
                .key("eclipse-formatter.xml")      // Replace with your object key (the file name in the S3 bucket)
                .tagging(tagging)
                .build();


        // Put the tags on the object
        PutObjectTaggingResponse response = s3Client.putObjectTagging(putObjectTaggingRequest);
        System.out.println("Tags have been successfully added: " + response);

        // Close the S3 client
        s3Client.close();
    }
}
