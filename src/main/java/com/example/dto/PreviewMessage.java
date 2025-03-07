package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreviewMessage extends Message {
    private String topic;
    private String vote;

    public PreviewMessage(String topic, String vote) {
        super("preview");
        this.topic = topic;
        this.vote = vote;
    }
}
