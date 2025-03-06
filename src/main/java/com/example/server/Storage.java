package com.example.server;

import com.example.server.entity.Topic;
import com.example.server.entity.Vote;
import com.example.server.exception.VoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    private static final Logger logger = LogManager.getLogger(Storage.class);
    private static volatile Storage instance;

    private List<Topic> topics;

    private Storage() {
        topics = new CopyOnWriteArrayList<>();
    }

    public static Storage getInstance() {
        if (instance == null) {
            synchronized (Storage.class) {
                if (instance == null) {
                    instance = new Storage();
                }
            }
        }
        return instance;
    }

    public void createTopic(String topic) throws VoteException {
        boolean isExist = topics.stream().noneMatch(t -> t.getName().equals(topic));
        if (!isExist) {
            logger.warn("Such topic is already exist.");
            throw new VoteException("Такой topic уже существует.");
        }

        topics.add(new Topic("", topic)); // #fixme вывод имени
    }

    public void createVote(String topic, String vote, String theme, List<String> answers) throws VoteException{
        Topic topicObj = topics.stream()
                .filter(t -> t.getName().equals(topic))
                .findFirst()
                .orElse(null);
        if (topicObj == null){
            logger.warn("Such topic is not exist.");
            throw new VoteException("Такого topic нет.");
        }
        topicObj.addVote("", vote, theme, answers); // #fixme вывод имени
    }

//    public void deleteVote(String topic, String vote){
//
//    }
    public String getTopics(){
        StringBuilder result = new StringBuilder();
        result.append("TOPICS\n");
        if (topics.isEmpty()) result.append("пусто");
        for (Topic topic: topics) {
            result.append(String.format("TOPIC   %s \n", topic.getName()));
            if (topic.getVotes().isEmpty()) result.append("пусто");
            for (Vote vote : topic.getVotes()) {
                result.append(String.format("vote   %s \n", vote.getName()));
            }
        }
        return result.toString().trim();
    }

    public String getTopic(String topic) throws VoteException {
        StringBuilder result = new StringBuilder();
        Topic topicObj = topics.stream()
                .filter(t -> t.getName().equals(topic))
                .findFirst()
                .orElse(null);
        if (topicObj == null)
            throw new VoteException("Такого topic нет.");

        result.append(String.format("TOPIC   %s \n", topicObj.getName()));
        if (topicObj.getVotes().isEmpty()) result.append("пусто");
        for (Vote vote : topicObj.getVotes())
            result.append(String.format("vote   %s \n", vote.getName()));
        return result.toString().trim();
    }

    public String getVote(String topic, String vote) throws VoteException {
        Topic topicObj = topics.stream()
                .filter(t -> t.getName().equals(topic))
                .findFirst()
                .orElse(null);
        if (topicObj == null)
            throw new VoteException("Такого topic нет.");
        Vote voteObj = topicObj.getVotes().stream()
                .filter(v -> v.getName().equals(vote))
                .findFirst()
                .orElse(null);
        if (voteObj == null)
            throw new VoteException("Такого vote нет.");

        StringBuilder result = new StringBuilder();
        result.append(String.format("VOTE  %s \n", voteObj.getName()));
        for (String ans : voteObj.getAnswers().keySet())
            result.append(String.format(" %s  - %d\n", ans, voteObj.getAnswers().get(ans)));
        return result.toString().trim();
    }
//    public void vote(String topic, String vote, String username){
//
//    }
}