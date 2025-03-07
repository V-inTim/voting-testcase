package com.example.server;

import com.example.server.command.FileCommand;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;


public class CommandProcessor {
    private VotingTcpServer server;
    Thread serverThread;

    private final Map<String, Consumer<String[]>> fileCommands = Map.of(
            "load", FileCommand::load,
            "save", FileCommand::save
    );


    public CommandProcessor(VotingTcpServer server, Thread serverThread) {
        this.server = server;
        this.serverThread = serverThread;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command, commandLine;

        do {
            commandLine = scanner.nextLine();
            String[] words = commandLine.split("\\s+");
            command = words[0];

            if (command.equals("exit")){
                performExit();
            } else if (fileCommands.containsKey(command)){
                fileCommands.get(command).accept(words);
            } else
                System.out.println(String.format("Команда %s не определена.", command));
        } while (!command.equals("exit"));
    }

    private void performExit() {
        server.close();
        serverThread.interrupt();
    }
}
