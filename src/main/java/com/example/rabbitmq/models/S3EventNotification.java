package com.example.rabbitmq.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class S3EventNotification {

    @JsonProperty("EventName")
    private String eventName;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("Records")
    private List<Record> records;

    // Getters and setters
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    // Record class
    public static class Record {
        @JsonProperty("eventVersion")
        private String eventVersion;

        @JsonProperty("eventSource")
        private String eventSource;

        @JsonProperty("awsRegion")
        private String awsRegion;

        @JsonProperty("eventTime")
        private String eventTime;

        @JsonProperty("eventName")
        private String eventName;

        @JsonProperty("userIdentity")
        private UserIdentity userIdentity;

        @JsonProperty("requestParameters")
        private RequestParameters requestParameters;

        @JsonProperty("responseElements")
        private ResponseElements responseElements;

        @JsonProperty("s3")
        private S3Details s3;

        @JsonProperty("source")
        private Source source;

        // Getters and setters
        public String getEventVersion() {
            return eventVersion;
        }

        public void setEventVersion(String eventVersion) {
            this.eventVersion = eventVersion;
        }

        public String getEventSource() {
            return eventSource;
        }

        public void setEventSource(String eventSource) {
            this.eventSource = eventSource;
        }

        public String getAwsRegion() {
            return awsRegion;
        }

        public void setAwsRegion(String awsRegion) {
            this.awsRegion = awsRegion;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public UserIdentity getUserIdentity() {
            return userIdentity;
        }

        public void setUserIdentity(UserIdentity userIdentity) {
            this.userIdentity = userIdentity;
        }

        public RequestParameters getRequestParameters() {
            return requestParameters;
        }

        public void setRequestParameters(RequestParameters requestParameters) {
            this.requestParameters = requestParameters;
        }

        public ResponseElements getResponseElements() {
            return responseElements;
        }

        public void setResponseElements(ResponseElements responseElements) {
            this.responseElements = responseElements;
        }

        public S3Details getS3() {
            return s3;
        }

        public void setS3(S3Details s3) {
            this.s3 = s3;
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        // UserIdentity class
        public static class UserIdentity {
            @JsonProperty("principalId")
            private String principalId;

            public String getPrincipalId() {
                return principalId;
            }

            public void setPrincipalId(String principalId) {
                this.principalId = principalId;
            }
        }

        // RequestParameters class
        public static class RequestParameters {
            @JsonProperty("principalId")
            private String principalId;

            @JsonProperty("region")
            private String region;

            @JsonProperty("sourceIPAddress")
            private String sourceIPAddress;

            // Getters and setters
            public String getPrincipalId() {
                return principalId;
            }

            public void setPrincipalId(String principalId) {
                this.principalId = principalId;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getSourceIPAddress() {
                return sourceIPAddress;
            }

            public void setSourceIPAddress(String sourceIPAddress) {
                this.sourceIPAddress = sourceIPAddress;
            }
        }

        // ResponseElements class
        public static class ResponseElements {
            @JsonProperty("x-amz-id-2")
            private String xAmzId2;

            @JsonProperty("x-amz-request-id")
            private String xAmzRequestId;

            @JsonProperty("x-minio-deployment-id")
            private String xMinioDeploymentId;

            @JsonProperty("x-minio-origin-endpoint")
            private String xMinioOriginEndpoint;

            // Getters and setters
            public String getxAmzId2() {
                return xAmzId2;
            }

            public void setxAmzId2(String xAmzId2) {
                this.xAmzId2 = xAmzId2;
            }

            public String getxAmzRequestId() {
                return xAmzRequestId;
            }

            public void setxAmzRequestId(String xAmzRequestId) {
                this.xAmzRequestId = xAmzRequestId;
            }

            public String getxMinioDeploymentId() {
                return xMinioDeploymentId;
            }

            public void setxMinioDeploymentId(String xMinioDeploymentId) {
                this.xMinioDeploymentId = xMinioDeploymentId;
            }

            public String getxMinioOriginEndpoint() {
                return xMinioOriginEndpoint;
            }

            public void setxMinioOriginEndpoint(String xMinioOriginEndpoint) {
                this.xMinioOriginEndpoint = xMinioOriginEndpoint;
            }
        }

        // S3Details class
        public static class S3Details {
            @JsonProperty("s3SchemaVersion")
            private String s3SchemaVersion;

            @JsonProperty("configurationId")
            private String configurationId;

            @JsonProperty("bucket")
            private Bucket bucket;

            @JsonProperty("object")
            private S3Object s3Object;

            // Getters and setters
            public String getS3SchemaVersion() {
                return s3SchemaVersion;
            }

            public void setS3SchemaVersion(String s3SchemaVersion) {
                this.s3SchemaVersion = s3SchemaVersion;
            }

            public String getConfigurationId() {
                return configurationId;
            }

            public void setConfigurationId(String configurationId) {
                this.configurationId = configurationId;
            }

            public Bucket getBucket() {
                return bucket;
            }

            public void setBucket(Bucket bucket) {
                this.bucket = bucket;
            }

            public S3Object getS3Object() {
                return s3Object;
            }

            public void setS3Object(S3Object s3Object) {
                this.s3Object = s3Object;
            }

            // Bucket class
            public static class Bucket {
                @JsonProperty("name")
                private String name;

                @JsonProperty("ownerIdentity")
                private OwnerIdentity ownerIdentity;

                @JsonProperty("arn")
                private String arn;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public OwnerIdentity getOwnerIdentity() {
                    return ownerIdentity;
                }

                public void setOwnerIdentity(OwnerIdentity ownerIdentity) {
                    this.ownerIdentity = ownerIdentity;
                }

                public String getArn() {
                    return arn;
                }

                public void setArn(String arn) {
                    this.arn = arn;
                }

                public static class OwnerIdentity {
                    @JsonProperty("principalId")
                    private String principalId;

                    public String getPrincipalId() {
                        return principalId;
                    }

                    public void setPrincipalId(String principalId) {
                        this.principalId = principalId;
                    }
                }
            }

            // S3Object class
            public static class S3Object {
                @JsonProperty("key")
                private String key;

                @JsonProperty("size")
                private long size;

                @JsonProperty("eTag")
                private String eTag;

                @JsonProperty("contentType")
                private String contentType;

                @JsonProperty("userMetadata")
                private UserMetadata userMetadata;

                @JsonProperty("sequencer")
                private String sequencer;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public long getSize() {
                    return size;
                }

                public void setSize(long size) {
                    this.size = size;
                }

                public String getETag() {
                    return eTag;
                }

                public void setETag(String eTag) {
                    this.eTag = eTag;
                }

                public String getContentType() {
                    return contentType;
                }

                public void setContentType(String contentType) {
                    this.contentType = contentType;
                }

                public UserMetadata getUserMetadata() {
                    return userMetadata;
                }

                public void setUserMetadata(UserMetadata userMetadata) {
                    this.userMetadata = userMetadata;
                }

                public String getSequencer() {
                    return sequencer;
                }

                public void setSequencer(String sequencer) {
                    this.sequencer = sequencer;
                }

                public static class UserMetadata {
                    @JsonProperty("content-type")
                    private String contentType;

                    public String getContentType() {
                        return contentType;
                    }

                    public void setContentType(String contentType) {
                        this.contentType = contentType;
                    }
                }
            }
        }

        // Source class
        public static class Source {
            @JsonProperty("host")
            private String host;

            @JsonProperty("port")
            private String port;

            @JsonProperty("userAgent")
            private String userAgent;

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public String getPort() {
                return port;
            }

            public void setPort(String port) {
                this.port = port;
            }

            public String getUserAgent() {
                return userAgent;
            }

            public void setUserAgent(String userAgent) {
                this.userAgent = userAgent;
            }
        }
    }
}