package com.example.client.command;

import com.example.client.exception.CommandException;
import com.example.dto.CreateTopicMessage;
import com.example.dto.CreateVoteMessage;
import com.example.dto.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VoteCommand {
    public static Message create(String[] args) {
        try {
            if (args.length != 3)
                throw new CommandException("Не подходящее количество аргументов.");
            String subcommand = args[1];

            if (subcommand.equals("topic") && args[2].startsWith("-n=")) {
                String topic = args[2].substring(3);
                return new CreateTopicMessage(topic);
            }
            if (subcommand.equals("vote") && args[2].startsWith("-t=")) {
                String topic = args[2].substring(3);
                return createVote(topic);
            }
            throw new CommandException("Неправильная форма команды create.");
        } catch (CommandException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
//    public static Message view(String[] args) {
//        return new Message();
//    }
//    public static Message vote(String[] args) {
//        return new Message();
//    }
//    public static Message delete(String[] args) {
//        return new Message();
//    }

    private static Message createVote(String topic){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название (уникальное имя):");
        String vote = scanner.nextLine();

        System.out.println("Введите тему голосования (описание):");
        String theme = scanner.nextLine();

        System.out.println("Введите количество вариантов ответа:");
        int n;
        try {
            n = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Некорректное численное значение. Попробуйте создать заново.");
            return null;
        }

        List<String> answers = new ArrayList<>();
        for (int i = 0; i < n; i++){
            System.out.println("Введите вариант ответа:");
            answers.add(scanner.nextLine());
        }
        return new CreateVoteMessage(topic, vote, theme, answers);
    }
}
