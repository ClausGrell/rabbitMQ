package com.example.rabbitmq.S3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        if (tags!=null) {
            tagList = new ArrayList(tags);
        }

        boolean tagReplaced = false;
        if (tagList!=null) {
            for (int i = 0; i < tagList.size(); i++) {
                var key = tagList.get(i).key();

                if (key.equals(tag)) {
                    var v = Tag.builder().key(tag).value(value).build();
                    tagList.set(i, v);
                    tagReplaced = true;
                }
            }
        }
        if (!tagReplaced) {
            tagList.add(Tag.builder().key(tag).value(value).build());
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

    public void getMetaData() {
        S3Client s3Client = getS3Client();
        try {
            // Get object metadata
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket("testbucket")
                    .key("Main.java")
                    .build();

            HeadObjectResponse metadata = s3Client.headObject(headObjectRequest);

            // Print metadata details
            System.out.println("Metadata for object: " + "Main.java");
            System.out.println("Content-Type: " + metadata.contentType());
            System.out.println("Last Modified: " + metadata.lastModified());
            System.out.println("Metadata (Custom Metadata): " + metadata.metadata());

        } catch (S3Exception e) {
            System.err.println("Error getting metadata: " + e.getMessage());
            System.exit(1);
        }
    }

    public void addMetaData() {
        // Prepare new metadata
        S3Client s3Client = getS3Client();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Author", "Jane Doe");
        metadata.put("Description", "Updated metadata for the file");
        metadata.put("Content-Type", "text/plain");

        // Step 1: Copy the object with new metadata
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket("testbucket")   // Original bucket
                .sourceKey("Main.java")         // Original object key
                .destinationBucket("testbucket")  // Destination bucket (same in this case)
                .destinationKey("Main.java")        // Destination key (same in this case)
                .metadata(metadata)          // Set the new metadata
                .metadataDirective("REPLACE") // This is important to indicate metadata should be replaced
                .build();

        CopyObjectResponse copyResponse = s3Client.copyObject(copyObjectRequest);
        System.out.println("Object copied with new metadata: " + copyResponse.copyObjectResult());


    }







    public void removeTag(String bucketName, String objectKey, String tag) {
        S3Client s3Client = getS3Client();
        var tags = getObjectTags(bucketName, objectKey );
        ArrayList<Tag> tagList = null;

        if (tags.isEmpty())
            tagList = new ArrayList<Tag>();
        else
            tagList = new ArrayList(tags);

        boolean tagRemoved = false;
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).key().equals(tag)) {
                tagList.remove(i);
                tagRemoved = true;

            }
            if (tagRemoved) {
                Tagging tagging = Tagging.builder().tagSet(tagList).build();

                PutObjectTaggingRequest putObjectTaggingRequest = PutObjectTaggingRequest.builder()
                        .bucket(bucketName)  // Replace with your bucket name
                        .key(objectKey)      // Replace with your object key (the file name in the S3 bucket)
                        .tagging(tagging)
                        .build();

                PutObjectTaggingResponse response = s3Client.putObjectTagging(putObjectTaggingRequest);
                logger.info("Tags have been successfully removed: " + response);
            }
        }
        s3Client.close();
    }



    public S3Client getS3Client() {
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
            return null;
        } else {
            for (Tag tag : tags) {
                logger.info("Tag key: " + tag.key() + ", value: " + tag.value());
            }
        }
        return tags;
    }

    public boolean isComplient(List<Tag> tags) {
        if (tags != null) {
            if (tags.isEmpty()) {
                logger.info("No tags found for the object.");
                return false;
            } else {
                for (Tag tag : tags) {
                    if ((tag.key().equals("Complient")) && (tag.value().equals("true"))) {
                        System.out.println();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
