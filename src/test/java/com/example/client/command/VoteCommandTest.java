package com.example.client.command;

import com.example.dto.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class VoteCommandTest {
    @Test
    public void testCreateTopic() {
        String[] args = {"create", "topic", "-n=New Topic"};
        Message message = VoteCommand.create(args);

        assertNotNull(message);
        assertTrue(message instanceof CreateTopicMessage);
        assertEquals("New Topic", ((CreateTopicMessage) message).getTopic());
    }


    @Test
    public void testCreateInvalidCommand() {
        String[] args = {"create", "vote", "-x=Invalid"};
        Message message = VoteCommand.create(args);

        assertNull(message);
    }

    @Test
    public void testViewWithoutArgs() {
        String[] args = {"view"};
        Message message = VoteCommand.view(args);

        assertNotNull(message);
        assertTrue(message instanceof ViewMessage);
    }

    @Test
    public void testViewTopic() {
        String[] args = {"view", "-t=Test Topic"};
        Message message = VoteCommand.view(args);

        assertNotNull(message);
        assertTrue(message instanceof ViewTopicMessage);
        assertEquals("Test Topic", ((ViewTopicMessage) message).getTopic());
    }

    @Test
    public void testViewVote() {
        String[] args = {"view", "-t=Test Topic", "-v=Test Vote"};
        Message message = VoteCommand.view(args);

        assertNotNull(message);
        assertTrue(message instanceof ViewVoteMessage);
        ViewVoteMessage viewVoteMessage = (ViewVoteMessage) message;
        assertEquals("Test Topic", viewVoteMessage.getTopic());
        assertEquals("Test Vote", viewVoteMessage.getVote());
    }

    @Test
    public void testViewInvalidCommand() {
        String[] args = {"view", "-x=Invalid"};
        Message message = VoteCommand.view(args);

        assertNull(message);
    }

    @Test
    public void testVote() {
        String[] args = {"vote", "-t=Test Topic", "-v=Test Vote"};
        Message message = VoteCommand.vote(args);

        assertNotNull(message);
        assertTrue(message instanceof PreviewMessage);
        PreviewMessage previewMessage = (PreviewMessage) message;
        assertEquals("Test Topic", previewMessage.getTopic());
        assertEquals("Test Vote", previewMessage.getVote());
    }

    @Test
    public void testVoteInvalidCommand() {
        String[] args = {"vote", "-t=Test Topic"};
        Message message = VoteCommand.vote(args);

        assertNull(message);
    }

    @Test
    public void testDelete() {
        String[] args = {"delete", "-t=Test Topic", "-v=Test Vote"};
        Message message = VoteCommand.delete(args);

        assertNotNull(message);
        assertTrue(message instanceof DeleteMessage);
        DeleteMessage deleteMessage = (DeleteMessage) message;
        assertEquals("Test Topic", deleteMessage.getTopic());
        assertEquals("Test Vote", deleteMessage.getVote());
    }

    @Test
    public void testDeleteInvalidCommand() {
        String[] args = {"delete", "-t=Test Topic"};
        Message message = VoteCommand.delete(args);

        assertNull(message);
    }


}
