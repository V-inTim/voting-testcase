package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewTopicMessage extends Message{
    private String topic;

    public ViewTopicMessage(String topic) {
        super("view topic");
        this.topic = topic;
    }
}
