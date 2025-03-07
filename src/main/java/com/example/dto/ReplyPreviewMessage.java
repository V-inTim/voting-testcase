package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReplyPreviewMessage extends ReplyMessage {
    private String topic;
    private String vote;
    private List<String> answers;

    public ReplyPreviewMessage(String response, String topic, String vote, List<String> answers) {
        super("preview response", response);
        this.topic = topic;
        this.vote = vote;
        this.answers = answers;
    }
}
