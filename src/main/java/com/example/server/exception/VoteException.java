package com.example.server.exception;

public class VoteException extends Exception{

    public VoteException() {
        super("Ошибка, связанная с базой голосований.");
    }

    public VoteException(String message) {
        super(message);
    }
}
