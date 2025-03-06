package com.example.client.command;

import com.example.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VoteCommand {
    public static Message create(String[] args) {
        if (args.length == 3){
            String subcommand = args[1];
            if (subcommand.equals("topic") && args[2].startsWith("-n=")) {
                String topic = args[2].substring(3);
                return new CreateTopicMessage(topic);
            }
            if (subcommand.equals("vote") && args[2].startsWith("-t=")) {
                String topic = args[2].substring(3);
                return createVote(topic);
            }
        }
        System.out.println("Неправильная форма команды create.");
        return null;
    }

    public static Message view(String[] args) {
        if (args.length == 1)
            return new ViewMessage();

        String subcommand = args[1];

        if (args.length == 2 && args[1].startsWith("-t=")) {
            String topic = args[1].substring(3);
            return new ViewTopicMessage(topic);
        }
        if (args.length == 3 && args[1].startsWith("-t=") && args[2].startsWith("-v=")) {
            String topic = args[1].substring(3);
            String vote = args[2].substring(3);
            return new ViewVoteMessage(topic, vote);
        }
        System.out.println("Неправильная форма команды view.");
        return null;
    }
//    public static Message vote(String[] args) {
//        return new Message();
//    }
    public static Message delete(String[] args) {
        if (args.length == 3 && args[1].startsWith("-t=") && args[2].startsWith("-v=")) {
            String topic = args[1].substring(3);
            String vote = args[2].substring(3);
            return new DeleteMessage(topic, vote);
        }
        System.out.println("Неправильная форма команды view.");
        return null;
    }

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
