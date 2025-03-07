package com.example.server;

public class ServerApp {


    public static void run() {
        VotingTcpServer server = new VotingTcpServer(8080);

        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (InterruptedException e) {
                System.out.println("Завершение работы сервера.");
            }
        });
        serverThread.start();

        CommandProcessor commandProcessor = new CommandProcessor(server, serverThread);
        commandProcessor.run();

    }
}
