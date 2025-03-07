package com.example;

import com.example.client.ClientApp;
import com.example.server.ServerApp;


public class App 
{
    public static void main( String[] args )
    {
        if (args.length != 1){
            System.out.println("Неподходящее число аргументов.");
            return;
        }

        switch (args[0]){
            case "client":
                System.out.println("Режим клиента.");
                ClientApp.run();
                break;
            case "server":
                System.out.println("Режим сервера.");
                ServerApp.run();
                break;
            default:
                System.out.println("Неизвестный режим.");
        }
    }
}
