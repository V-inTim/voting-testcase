package com.example.server;

import com.example.server.exception.VoteException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StorageTest {
    private Storage storage = Storage.getInstance();




    @Test
    public void testCreateTopicThrowsException() throws VoteException {
        storage.createTopic("Topic1", "User1");
        assertThrows(VoteException.class, () -> storage.createTopic("Topic1", "User2"));
    }

    @Test
    public void testCreateVoteThrowsException() {
        assertThrows(VoteException.class, () ->
                storage.createVote("NonExistentTopic", "Vote1", "Theme1", List.of("Answer1", "Answer2"), "User1"));
    }

    @Test
    public void testGetTopic() throws VoteException {
        storage.createVote("Topic1", "Vote1", "Theme1", List.of("Answer1", "Answer2"), "User1");
        String expected = "TOPIC   Topic1 \nvote   Vote1 \n";
        assertEquals(expected.trim(), storage.getTopic("Topic1").trim());
    }

    @Test
    public void testGetTopicThrowsException() {
        assertThrows(VoteException.class, () -> storage.getTopic("NonExistentTopic"));
    }

    @Test
    public void testPreviewThrowsException() throws VoteException {
        assertThrows(VoteException.class, () -> storage.preview("Topic1", "NonExistentVote"));
    }

}
