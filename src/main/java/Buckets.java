import com.example.rabbitmq.config.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;



import java.net.URI;
import java.util.EnumSet;


public class Buckets {

    @Autowired
    S3Config s3Config;

    String s3url = "http://192.168.0.184:9000";
    String region = "us-west-2";
    String accesskeyid = "PQZEgUSS3wMMtSubdBo6";
    String secretaccesskey = "B8OXpqAo3g31KY3XcTQBk3R6oap2lUTbCh5jFzMK";

    public static void main(String[] args) {
        System.out.println("Hello");
        Buckets buckets = new Buckets();
        buckets.run();


    }

    public void run() {
        S3Client s3Client = getS3Client();
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

}
