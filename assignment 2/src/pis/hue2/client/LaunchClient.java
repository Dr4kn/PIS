package pis.hue2.client;

import java.io.*;
import java.net.*;

public class LaunchClient
{
    private static final int PORT = 42069;

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

            // sends out to the server rewrite as necessary
            out.println(command);

            // the server response get it to whichever way is necessary to display the things properly
            String serverResponse = input.readLine();

            System.out.println("Server: " + serverResponse);
        }
    }
}
