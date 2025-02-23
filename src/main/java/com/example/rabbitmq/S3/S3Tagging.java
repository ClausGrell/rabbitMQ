package com.example.rabbitmq.S3;

import jakarta.servlet.ServletOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.metrics.StartupStep;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.set;


public class S3Tagging {

    private static final Logger logger = LoggerFactory.getLogger(S3Tagging.class);

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
        var tags = getObjectTags(bucketName, objectKey );
        ArrayList<Tag> tagList = new ArrayList(tags);

        boolean tagReplaced = false;
        for (int i = 0; i < tagList.size(); i++) {
            var key = tagList.get(i).key();

            if (key.equals(tag)) {
                var v = Tag.builder().key(tag).value(value).build();
                tagList.set(i,v);
                tagReplaced = true;
            }
        }
        if (!tagReplaced) {
            tags.add(Tag.builder().key(tag).value(value).build());
        }
        Tagging tagging = Tagging.builder().tagSet(tagList).build();

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

    public List<Tag> getObjectTags(String bucketName, String objectKey) {
        S3Client s3Client = getS3Client();
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
        return tags;
    }

    public boolean isComplient(List<Tag> tags) {
        if (tags.isEmpty()) {
            logger.info("No tags found for the object.");
        } else {
            for (Tag tag : tags) {
                if ((tag.key().equals("Complient")) && (tag.value().equals("true")))  {
                    System.out.println();
                    return true;
                }
            }
        }
        return false;
    }
}
