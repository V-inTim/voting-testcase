package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsernameMessage extends Message {
    private String username;

    public UsernameMessage(String username) {
        super("username");
        this.username = username;
    }
}
