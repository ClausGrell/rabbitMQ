package com.example.rabbitmq.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class S3Event {
    @JsonProperty("EventName") // Explicit mapping for "EventName"
    private String eventName;

    @JsonProperty("Key") // Explicit mapping for "Key"
    private String key;

    @JsonProperty("Records") // Explicit mapping for "Records"
    private List<Record> records;

    // Getters and Setters
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

    public static class Record {
        private String eventVersion;
        private String eventSource;
        private String awsRegion;
        private String eventTime;
        private String eventName;
        private UserIdentity userIdentity;
        private RequestParameters requestParameters;
        private ResponseElements responseElements;
        private S3 s3;
        private Source source;

        // Getters and Setters

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

        public S3 getS3() {
            return s3;
        }

        public void setS3(S3 s3) {
            this.s3 = s3;
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public static class UserIdentity {
            private String principalId;

            // Getter and Setter
            public String getPrincipalId() {
                return principalId;
            }

            public void setPrincipalId(String principalId) {
                this.principalId = principalId;
            }
        }

        public static class RequestParameters {
            private String principalId;
            private String region;
            private String sourceIPAddress;

            // Getters and Setters
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

        public static class ResponseElements {
            private String xAmzId2;
            private String xAmzRequestId;
            private String xMinioDeploymentId;
            private String xMinioOriginEndpoint;

            // Getters and Setters
            public String getXAmzId2() {
                return xAmzId2;
            }

            public void setXAmzId2(String xAmzId2) {
                this.xAmzId2 = xAmzId2;
            }

            public String getXAmzRequestId() {
                return xAmzRequestId;
            }

            public void setXAmzRequestId(String xAmzRequestId) {
                this.xAmzRequestId = xAmzRequestId;
            }

            public String getXMinioDeploymentId() {
                return xMinioDeploymentId;
            }

            public void setXMinioDeploymentId(String xMinioDeploymentId) {
                this.xMinioDeploymentId = xMinioDeploymentId;
            }

            public String getXMinioOriginEndpoint() {
                return xMinioOriginEndpoint;
            }

            public void setXMinioOriginEndpoint(String xMinioOriginEndpoint) {
                this.xMinioOriginEndpoint = xMinioOriginEndpoint;
            }
        }

        public static class S3 {
            private String s3SchemaVersion;
            private String configurationId;
            private Bucket bucket;
            private Object object;

            // Getters and Setters
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

            public Object getObject() {
                return object;
            }

            public void setObject(Object object) {
                this.object = object;
            }

            public static class Bucket {
                private String name;
                private OwnerIdentity ownerIdentity;
                private String arn;

                // Getters and Setters
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
                    private String principalId;

                    // Getter and Setter
                    public String getPrincipalId() {
                        return principalId;
                    }

                    public void setPrincipalId(String principalId) {
                        this.principalId = principalId;
                    }
                }
            }

            public static class Object {
                private String key;
                private long size;
                private String eTag;
                private String contentType;
                private UserMetadata userMetadata;
                private String sequencer;

                // Getters and Setters
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

                public String geteTag() {
                    return eTag;
                }

                public void seteTag(String eTag) {
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
                    private String contentType;

                    // Getter and Setter
                    public String getContentType() {
                        return contentType;
                    }

                    public void setContentType(String contentType) {
                        this.contentType = contentType;
                    }
                }
            }
        }

        public static class Source {
            private String host;
            private String port;
            private String userAgent;

            // Getters and Setters
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
    }}
