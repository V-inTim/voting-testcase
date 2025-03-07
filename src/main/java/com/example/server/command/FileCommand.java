package com.example.server.command;


import com.example.server.Storage;

import java.io.IOException;

public class FileCommand {
    private static final Storage storage = Storage.getInstance();
    public static void load(String[] args){
        if (args.length == 2) {
            String path = args[1];
            try {
                storage.loadTopicsFromJson(path);
            } catch (IOException e) {
                System.out.printf("Ошибка чтения: %s", e.getMessage());
            }
        } else
            System.out.println("Неправильная форма команды load.");
    }
    public static void save(String[] args){
        if (args.length == 2) {
            String path = args[1];
            try {
                storage.saveTopicsToJson(path);
            } catch (IOException e) {
                System.out.printf("Ошибка записи: %s", e.getMessage());
            }
        } else
            System.out.println("Неправильная форма команды save.");
    }
}
