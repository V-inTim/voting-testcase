package com.example.server;

import com.example.dto.*;
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
            "create vote", this::createVote,
            "view", this::view,
            "view topic", this::viewTopic,
            "view vote", this::viewVote,
            "delete", this::delete
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
            storage.createTopic(message.getTopic(), username);
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Topic created.");
        return new ReplyMessage("Topic создан.");
    }

    private Message createVote(Message msg){
        CreateVoteMessage message = (CreateVoteMessage) msg;

        try {
            storage.createVote(message.getTopic(), message.getVote(), message.getTheme(),
                    message.getAnswers(), username);
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Vote created.");
        return new ReplyMessage("Vote создан.");
    }

    private Message view(Message msg){
        ViewMessage message = (ViewMessage) msg;
        String result = storage.getTopics();
        logger.info("Get topics.");
        return new ReplyMessage(result);
    }

    private Message viewTopic(Message msg){
        ViewTopicMessage message = (ViewTopicMessage) msg;
        String result;
        try {
            result = storage.getTopic(message.getTopic());
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Get topic.");
        return new ReplyMessage(result);
    }

    private Message viewVote(Message msg){
        ViewVoteMessage message = (ViewVoteMessage) msg;
        String result;
        try {
            result = storage.getVote(message.getTopic(), message.getVote());
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Get vote.");
        return new ReplyMessage(result);
    }

    private Message delete(Message msg){
        DeleteMessage message = (DeleteMessage) msg;
        try {
            storage.delete(message.getTopic(), message.getVote(), username);
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Delete vote.");
        return new ReplyMessage("Голосование удалено.");
    }

    private Message getVote(Message msg){
        PreviewMessage message = (PreviewMessage) msg;
        String result;
        try {
            result = storage.getVote(message.getTopic(), message.getVote());
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Get vote.");
        return new ReplyMessage(result);
    }

    private Message vote(Message msg){
        DeleteMessage message = (DeleteMessage) msg;
        try {
            storage.delete(message.getTopic(), message.getVote(), username);
        } catch (VoteException e) {
            return new ReplyMessage(e.getMessage());
        }
        logger.info("Voice wrote.");
        return new ReplyMessage("Голос записан.");
    }
}
