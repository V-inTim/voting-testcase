package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteMessage extends Message{
    private String topic;
    private String vote;

    public DeleteMessage(String topic, String vote) {
        super("delete");
        this.topic = topic;
        this.vote = vote;
    }
}
