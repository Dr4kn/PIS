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

            switch (command)
            {
                case "CON":
                    out.println("CON");
                    String serverResponseCON = input.readLine();
                    System.out.println(serverResponseCON);
                    if(serverResponseCON.equals("ACK")){
                        System.out.println("Server: " + "Your connection was accepted.");
                    }
                    else if(serverResponseCON.equals("DND")){
                        System.out.println("Server: " + "There was an error trying to connect to the server.");
                    }
                    else{
                        System.out.println("Server: " + "Didn't get your request.");
                    }
                    break;

                case "DEL":
                    out.println("DEL");
                    String serverResponseDEL = input.readLine();
                    if(serverResponseDEL.equals("ACK")){
                        System.out.println("Server: " + "Your delete request was accepted.");
                    }
                    else if(serverResponseDEL.equals("DND")){
                        System.out.println("Server: " + "There was an error trying to delete the file from the server.");
                    }
                    else{
                        System.out.println("Server: " + "Didn't get your request.");
                    }
                    break;

                case "DSC":
                    out.println("DSC");
                    String serverResponseDSC = input.readLine();
                    if(serverResponseDSC.equals("DSC")){
                        System.out.println("Server: " + "You were disconnected successfully.");
                    }
                    else{
                        System.out.println("Server: " + "There was an error while trying to disconnect you.");
                    }
                    break;

                case "GET":
                    out.println("GET");
                    String serverResponseGET = input.readLine();
                    if(serverResponseGET.equals("ACK")){
                        System.out.println("Server: " + "Your get request was accepted.");
                        out.println("ACK");
                        String serverResponseGET2 = input.readLine(); // This is the data we wanted to get.
                        out.println("ACK");
                    }
                    else if(serverResponseGET.equals("DND")){
                        System.out.println("Server: " + "There was an error trying to get the file from the server.");
                    }
                    else{
                        System.out.println("Server: " + "Didn't get your request.");
                    }
                    break;

                case "LST":
                    out.println("LST");
                    String serverResponseLST = input.readLine();
                    if(serverResponseLST.equals("DSC")){
                        System.out.println("Server: " + "You were disconnected successfully.");
                    }
                    else{
                        System.out.println("Server: " + "There was an error while trying to disconnect you.");
                    }
                    break;

                case "PUT":
                    out.println("PUT");
                    String serverResponsePUT = input.readLine();
                    if(serverResponsePUT.equals("DSC")){
                        System.out.println("Server: " + "You were disconnected successfully.");
                    }
                    else{
                        System.out.println("Server: " + "There was an error while trying to disconnect you.");
                    }

                    break;

                default:
                    System.out.println("Server didn't respond.");
                    break;

            }

            if (command.equals("quit")) break;


        }
    }
}
