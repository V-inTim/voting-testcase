package com.example.client.exception;

public class CommandException extends Exception {
    public CommandException() {
        super("Ошибка, связанная с командой.");
    }

    public CommandException(String message) {
        super(message);
    }
}
