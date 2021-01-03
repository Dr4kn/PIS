package pis.hue2.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LaunchServer
{
    public static void main(String[] args) throws Exception
    {
        int number;

        ServerSocket serverSocket = new ServerSocket(42069);
        System.out.println("Server is running");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        Scanner scanner = new Scanner(socket.getInputStream());
        number = scanner.nextInt();
        System.out.println("Server received the number: " + number);

        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(7);
    }
}
