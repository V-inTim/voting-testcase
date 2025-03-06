package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateVoteMessage extends Message{
    private String topic;
    private String vote;
    private String theme;
    private List<String> answers;

    public CreateVoteMessage(String topic, String vote, String theme, List<String> answers) {
        super("create vote");
        this.topic = topic;
        this.vote = vote;
        this.theme = theme;
        this.answers = answers;
    }
}
