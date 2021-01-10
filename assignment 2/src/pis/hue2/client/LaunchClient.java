package pis.hue2.client;

import pis.hue2.fileSending.FileSender;

import java.io.*;
import java.net.*;

public class LaunchClient
{
    private static final int PORT = 42069;
    private static final String filePath = "testFiles/client/";
    private static String getFileName = "";
    private static String delFileName = "";
    private static String command;



    public static void main(String[] args) throws Exception
    {
        Socket socket = new Socket("localhost", PORT);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // creates Thread and starts GUI
        StartGUI startGUI = new StartGUI();
        startGUI.start();

        boolean isRunning = true;

        while (isRunning)
        {
            System.out.print(">");

            // "command" should be your way of sending things to the server
            // keyboard.read line can be replaced and is only here for testing purposes
            command = keyboard.readLine();

            if (command.contains("GET"))
            {
                try
                {
                    StringBuilder getFile = new StringBuilder();
                    for (char ch : command.substring(4).toCharArray())
                    {
                        getFile.append(ch);
                    }
                    getFileName = getFile.toString();
                    command = command.split(" ")[0];
                }
                catch (StringIndexOutOfBoundsException e)
                {
                    System.out.println("Must Specify the File in the Get Request");
                }
            }

            if (command.contains("DEL"))
            {
                try
                {
                    StringBuilder delFile = new StringBuilder();
                    for (char ch : command.substring(4).toCharArray())
                    {
                        delFile.append(ch);
                    }
                    delFileName = delFile.toString();
                    command = command.split(" ")[0];
                }
                catch (StringIndexOutOfBoundsException e)
                {
                    System.out.println("Must Specify the File in the DEL Request");
                }
            }

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
                        System.out.println("Server: " + "The server denied your request.");
                    }
                    else{
                        System.out.println("Server: " + "Didn't get your request.");
                    }
                    break;

                case "DEL":
                    out.println("DEL " + delFileName);
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
                        isRunning = false;
                    }
                    else{
                        System.out.println("Server: " + "There was an error while trying to disconnect you.");
                    }
                    break;

                case "GET":
                    out.println("GET " + getFileName);
                    String serverResponseGET = input.readLine();
                    if(serverResponseGET.equals("ACK")){
                        System.out.println("Server: " + "Your get request was accepted.");
                        out.println("ACK");
                        try
                        {
                            FileSender fileSender = new FileSender(filePath, socket);
                            fileSender.receiveFile();
                            out.println("ACK");
                        }
                        catch(Exception e)
                        {
                            out.println("DND");
                            System.out.println("File Transfer went wrong");
                        }
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
                    if(serverResponseLST.equals("ACK"))
                    {
                        out.println("ACK");

                        StringBuilder allFiles = new StringBuilder();
                        for (char ch : input.readLine().toCharArray())
                        {
                            if(ch == '|')
                            {
                                allFiles.append('\n');
                            }
                            else
                            {
                                allFiles.append(ch);
                            }
                        }
                        System.out.println(allFiles.toString());

                        out.println("ACK");
                    }
                    else
                    {
                        System.out.println("Server: " + "There was an error while trying to list the data.");
                    }
                    break;

                case "PUT":
                    out.println("PUT");
                    String serverResponsePUT = input.readLine();

                    if(serverResponsePUT.equals("ACK"))
                    {
                        FileSender fileSender = new FileSender(filePath + "test1.txt", socket);
                        fileSender.sendFile();
                    }

                    if(serverResponsePUT.equals("ACK"))
                    {
                        System.out.println("The File Transfer was successful");
                    }
                    else if(serverResponsePUT.equals("DND"))
                    {
                        System.out.println("File Transfer wasn't successful");
                    }
                    else
                    {
                        System.out.println("unknown command in put");
                    }
                    break;

                default:
                    System.out.println("Server didn't respond.");
                    break;

            }
        }
    }
}
