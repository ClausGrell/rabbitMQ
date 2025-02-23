package com.example.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "s3config")
public class S3Config {

    private String s3url;
    private String region;
    private String accesskeyid;
    private String secretaccesskey;

    public String getS3url() {
        return s3url;
    }

    public void setS3url(String s3url) {
        this.s3url = s3url;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccesskeyid() {
        return accesskeyid;
    }

    public void setAccesskeyid(String accesskeyid) {
        this.accesskeyid = accesskeyid;
    }

    public String getSecretaccesskey() {
        return secretaccesskey;
    }

    public void setSecretaccesskey(String secretaccesskey) {
        this.secretaccesskey = secretaccesskey;
    }
}
