package pis.hue2.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LaunchClient
{
    public static void main(String[] args) throws Exception
    {
        int number;
        Socket socket = new Socket("localhost", 42069);
        Scanner socketScanner = new Scanner(socket.getInputStream());
        System.out.println("Connected to Host");

        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(42);

        number = socketScanner.nextInt();
        System.out.println("Server send the number: " + number);
    }
}
