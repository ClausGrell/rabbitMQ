package com.example.rabbitmq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.Tag;

import java.util.List;

public class TimeValidator implements CValidater {

    private static final Logger logger = LoggerFactory.getLogger(TimeValidator.class);
    private List<Tag> tags;

    public TimeValidator(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean validate() {
        if (tags.isEmpty()) {
            logger.info("No tags found.");
            return false;
        } else {
            for (Tag tag : tags) {
                if (tag.key().equals("time") && (tag.value()!=null)) {
                    // check for valid time and for future
                    return true;
                }
            }
        }
        return false;
    }
}
