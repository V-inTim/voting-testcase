package com.example.client;


public class ClientApp {

    public static void run() {
        VotingTcpClient client = new VotingTcpClient("localhost", 8080);
        CommandProcessor commandProcessor = new CommandProcessor(client);
        commandProcessor.run();



    }

}
