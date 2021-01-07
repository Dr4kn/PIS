package pis.hue2.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LaunchClient
{
    private static int PORT = 42069;

    public static void main(String[] args) throws Exception
    {
        Socket socket = new Socket("localhost", PORT);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true)
        {
            System.out.print(">");

            // "command" should be your way of sending things to the server
            // keyboard.read line can be replaced and is only here for testing purposes
            String command = keyboard.readLine();

            if (command.equals("quit")) break;

            out.println(command);


            String serverResponse = input.readLine();
            // use serverResponse in another class to make the client do its things
            // this way i can edit this class later without causing problems in your code
            System.out.println("Server: " + serverResponse);
        }
    }
}
