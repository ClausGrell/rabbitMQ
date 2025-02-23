package com.example.rabbitmq.S3;

import com.example.rabbitmq.service.MessageListenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.List;


public class S3Tagging {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerService.class);

    private String accessKeyId;
    private String secretaccesskey;
    private String s3url;
    private String region;

    public S3Tagging(String s3url, String region, String accessKeyId, String secretaccesskey) {
        this.s3url = s3url;
        this.region = region;
        this.accessKeyId = accessKeyId;
        this.secretaccesskey = secretaccesskey;
    }

    public void doSomeTagging(String bucketName, String objectKey, String tag, String value) {
        S3Client s3Client = getS3Client();

        List<Tag> tags = List.of(
                Tag.builder().key(tag).value(value).build()
//             ,Tag.builder().key("").value("").build()
        );
        Tagging tagging = Tagging.builder().tagSet(tags).build();

        PutObjectTaggingRequest putObjectTaggingRequest = PutObjectTaggingRequest.builder()
                .bucket(bucketName)  // Replace with your bucket name
                .key(objectKey)      // Replace with your object key (the file name in the S3 bucket)
                .tagging(tagging)
                .build();

        PutObjectTaggingResponse response = s3Client.putObjectTagging(putObjectTaggingRequest);
        logger.info("Tags have been successfully added: " + response);
        s3Client.close();
    }

    private S3Client getS3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretaccesskey);
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(s3url))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds)) // Credentials (use IAM, or set up profile)
                .region(Region.of(region))  // Replace with your region
                .build();
        return s3Client;
    }

    public void getObjectTags(String bucketName, String objectKey) {
        S3Client s3Client = getS3Client();
        try {
            GetObjectTaggingRequest request = GetObjectTaggingRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            GetObjectTaggingResponse response = s3Client.getObjectTagging(request);

            List<Tag> tags = response.tagSet();
            if (tags.isEmpty()) {
                logger.info("No tags found for the object.");
            } else {
                for (Tag tag : tags) {
                    logger.info("Tag key: " + tag.key() + ", value: " + tag.value());
                }
            }
        } catch (S3Exception e) {
            logger.info("Error retrieving tags: " + e.getMessage());
        }
    }

}
