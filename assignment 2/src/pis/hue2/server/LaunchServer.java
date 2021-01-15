package pis.hue2.server;

import java.net.*;
import java.util.concurrent.*;

public class LaunchServer
{
    private static final int PORT = 42069;
    private static int numberOfClients = 0;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * @param args arguments
     * @throws Exception if the server isn't stopped properly
     * opens a port at 42069 and waits for clients to connect
     * if a client connects it starts a thread up so the client
     * can talk to the server
     */
    public static void main(String[] args) throws Exception
    {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server is running");
        try
        {
            while (true)
            {
                Socket client = listener.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(client);

                executorService.execute(clientHandler);
            }
        }
        catch(Exception e)
        {
            System.out.println("exception in main");
        }
    }

    /**
     * @return number of clients currently on the server
     */
    public static int getNumberOfClients()
    {
        return numberOfClients;
    }

    /**
     * should be used if a client is connected
     * keeps track of the number of users connected
     */
    public static void increaseNumberOfClients()
    {
        ++numberOfClients;
    }

    /**
     * should be used if a client is disconnected
     * keeps track of the number of users connected
     */
    public static void decreaseNumberOfClients()
    {
        --numberOfClients;
    }
}