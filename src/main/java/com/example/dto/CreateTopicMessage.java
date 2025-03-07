package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTopicMessage extends Message{
    private String topic;

    public CreateTopicMessage(String topic) {
        super("create topic");
        this.topic = topic;
    }
}
