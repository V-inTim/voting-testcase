package com.example.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    String creator;
    String name;
    String theme;
    Map<String, Integer> answers;

    public Vote(String creator, String name, String theme, List<String> answers) {
        this.creator = creator;
        this.name = name;
        this.theme = theme;
        this.answers = new HashMap<>();
        answers.forEach(key -> this.answers.put(key, 0));
    }
}
