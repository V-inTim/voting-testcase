package com.example.server.entity;

import com.example.server.exception.VoteException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Topic {
    private String name;
    private String creator;
    private List<Vote> votes;

    public Topic(String creator, String name) {
        this.creator = creator;
        this.name = name;
        this.votes = new ArrayList<>();
    }

    public void addVote(String creator, String name, String theme, List<String> answers) throws VoteException {
        boolean isExist = votes.stream().noneMatch(t -> t.getName().equals(name));
        if (!isExist)
            throw new VoteException("Такой topic уже существует.");
        votes.add(new Vote(creator, name, theme, answers));
    }
}

