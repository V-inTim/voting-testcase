package com.example.client;

import com.example.client.command.VoteCommand;
import com.example.client.exception.CommandException;
import com.example.dto.Message;
import com.example.dto.ReplyPreviewMessage;
import com.example.dto.VoteMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandProcessor {
    private VotingTcpClient client;
    private Thread clientThread;
    private final Scanner scanner = new Scanner(System.in);
    private volatile Boolean isBlocking;

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
        this.isBlocking = false;
    }

    public void run() {
        String command = "", commandLine;

        do {
            if(!isBlocking) {
                commandLine = scanner.nextLine();
                String[] words = commandLine.split("\\s+");
                command = words[0];

                if (!(client.checkLogin() || command.equals("login"))) {
                    System.out.println("Пользователь не авторизован.");
                } else if (serviceCommands.containsKey(command) || voteCommands.containsKey(command)) {
                    process(command, words);
                } else
                    System.out.println(String.format("Команда %s не определена.", command));
            }
        } while (!command.equals("exit"));
    }

    private void process(String command, String[] words){
        if (serviceCommands.containsKey(command)){
            serviceCommands.get(command).accept(words);
        } else {
            Message message = voteCommands.get(command).apply(words);
            if (message == null)
                return;
            if (command.equals("vote")){
                isBlocking = true;
                client.sendMessageAsync(message).thenAccept(this::vote).exceptionally(e -> {
                    System.out.println("Ошибка при запросе: " + e.getMessage());
                    isBlocking = false;
                    return null;
                });
            } else {
                client.sendMessage(message);
            }
        }
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
    private void vote(String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        ReplyPreviewMessage replyMessage;
        try {
            replyMessage = objectMapper.readValue(msg, ReplyPreviewMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<String> answers = replyMessage.getAnswers();
        if (answers == null)
            return;

        for (int i = 0; i < answers.size(); i++)
            System.out.printf("%d  %s\n", i + 1, answers.get(i));
        int a;
        try {
            a = Integer.parseInt(scanner.nextLine());
            isBlocking = false;
            if (a < 1 || a > answers.size())
                throw new CommandException();
        } catch (NumberFormatException e) {
            System.out.println("Некорректное численное значение. Попробуйте начать заново.");
            return;
        } catch (CommandException e){
            System.out.println("Число не относится к голосованию. Попробуйте начать заново.");
            return;
        }
        System.out.printf("Выбран вариант: %d\n", a);
        VoteMessage voteMessage = new VoteMessage(replyMessage.getTopic(), replyMessage.getVote(), answers.get(a - 1));
        client.sendMessage(voteMessage);
    }

    private void performExit(String[] args) {
        client.disconnect();
        clientThread.interrupt();
    }
}
