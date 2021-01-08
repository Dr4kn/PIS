package pis.hue2.server;

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaunchServer
{
    private static final int PORT = 42069;
    private static int numberOfClients = 0;

    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception
    {
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

    public static int getNumberOfClients()
    {
        return numberOfClients;
    }

    public static void increaseNumberOfClients()
    {
        ++numberOfClients;
    }

    public static void decreaseNumberOfClients()
    {
        --numberOfClients;
    }
}