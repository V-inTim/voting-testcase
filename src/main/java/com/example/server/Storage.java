package com.example.server;

import com.example.server.entity.Topic;
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
//    public void getTopics(){
//
//    }
//    public void getTopic(String topic){
//
//    }
//    public void getVote(String topic){
//
//    }
//    public void vote(String topic, String vote, String username){
//
//    }
}