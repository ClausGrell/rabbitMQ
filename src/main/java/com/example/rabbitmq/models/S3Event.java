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
}
