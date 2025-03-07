package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewVoteMessage extends Message{
    private String topic;
    private String vote;

    public ViewVoteMessage(String topic, String vote) {
        super("view vote");
        this.topic = topic;
        this.vote = vote;
    }
}
