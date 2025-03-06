package com.example.server;

import com.example.dto.CreateTopicMessage;
import com.example.dto.CreateVoteMessage;
import com.example.dto.Message;
import com.example.dto.ReplyMessage;
import com.example.server.exception.VoteException;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.Function;

public class RequestProcessor {
    private static final Logger logger = LogManager.getLogger(RequestProcessor.class);
    private Storage storage;
    @Setter
    private String username;

    private final Map<String, Function<Message, Message>> voteCommands = Map.of(
            "create topic", this::createTopic,
            "create vote", this::createVote
    );

    public RequestProcessor() {
        this.storage = Storage.getInstance();
    }


    public Message process(Message message){
        logger.info("Command Processing '{}'",message.getType());
        return voteCommands.getOrDefault(message.getType(), (msg) -> {
            return new ReplyMessage("Неизвестная команда.");
        }).apply(message);
    }

    private Message createTopic(Message msg){
        CreateTopicMessage message = (CreateTopicMessage) msg;

        try {
            storage.createTopic(message.getTopic());
        } catch (VoteException e) {
            logger.warn(e.getMessage());
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Topic created.");
        return new ReplyMessage("Topic создан.");
    }

    private Message createVote(Message msg){
        CreateVoteMessage message = (CreateVoteMessage) msg;

        try {
            storage.createVote(message.getTopic(), message.getVote(), message.getTheme(), message.getAnswers());
        } catch (VoteException e) {
            logger.warn(e.getMessage());
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Vote created.");
        return new ReplyMessage("Vote создан.");
    }
}
