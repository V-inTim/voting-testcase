package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyMessage extends Message {
    private String response;

    public ReplyMessage(String response) {
        super("response");
        this.response = response;
    }
}
