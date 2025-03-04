package com.example.client;

import com.example.client.command.VoteCommand;
import com.example.client.dto.Message;
import com.example.client.exception.CommandException;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandProcessor {
    private VotingTcpClient client;
    Thread clientThread;

    private final Map<String, Consumer<String[]>> serviceCommands = Map.of(
            "login", this::login,
            "exit", this::performExit
    );
    private final Map<String, Function<String[], Message>> voteCommands = Map.of(
            "create", VoteCommand::create,
            "view", VoteCommand::view,
            "vote", VoteCommand::vote,
            "delete", VoteCommand::delete
    );


    public CommandProcessor(VotingTcpClient client) {
        this.client = client;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command, commandLine;

        do {
            commandLine = scanner.nextLine();
            String[] words = commandLine.split("\\s+");
            command = words[0];

            if (serviceCommands.containsKey(command)){
                serviceCommands.get(command).accept(words);
                continue;
            }
            if (voteCommands.containsKey(command)){
                Message message = voteCommands.get(command).apply(words);
                continue;
            }
            System.out.println(String.format("Команда %s не определена", command));
        } while (!command.equals("exit"));
    }

    private void login(String[] args) {
        try {
            if (client.checkLogin())
                throw new CommandException("Пользователь уже авторизирован.");
            if (args.length != 2)
                throw new CommandException("Не подходящее количество аргументов.");
            if (!args[1].startsWith("-u="))
                throw new CommandException("Не указан обязательный аргумент -u= в команде login.");
        } catch(CommandException e) {
            System.out.println(e.getMessage());
            return;
        }

        String username = args[1].substring(3);

        clientThread = new Thread(() -> {
            try {
                client.start(username);
            } catch (InterruptedException e) {
                System.out.println("Завершение работы клиента.");
            }
        });
        clientThread.start();
    }

    private void performExit(String[] args) {
        client.disconnect();
        clientThread.interrupt();
    }
}
