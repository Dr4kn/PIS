package pis.hue2.server;

import pis.hue2.client.ClientHandler;

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaunchServer
{
    private static int PORT = 42069;

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception
    {
        int number;

        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server is running");

        while (true)
        {
            Socket client = listener.accept();
            System.out.println("Client connected");
            ClientHandler clientThread = new ClientHandler(client);

            clients.add(clientThread);
            executorService.execute(clientThread);
        }
    }
}