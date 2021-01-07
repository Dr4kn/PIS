package pis.hue2.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LaunchServer
{
    private static int PORT = 42069;
    public static void main(String[] args) throws Exception
    {
        int number;

        ServerSocket listener = new ServerSocket(PORT);

        System.out.println("Server is running");
        Socket client = listener.accept();
        System.out.println("Client connected");

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));


        try
        {
            while(true)
            {
                String request = in.readLine();
                if (request.contains("test"))
                {
                    out.println("Test was successful");
                }
                else
                {
                    out.println("Your message was " + request);
                }

                System.out.println("Server send message");
            }
        }
        finally
        {
            out.close();
            in.close();
        }

//        listener.close();
//        client.close();
    }
}