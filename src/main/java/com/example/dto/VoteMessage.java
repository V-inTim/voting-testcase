package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteMessage extends Message{
    private String topic;
    private String vote;
    private String answer;

    public VoteMessage(String topic, String vote, String answer) {
        super("vote");
        this.topic = topic;
        this.vote = vote;
        this.answer = answer;
    }
}
