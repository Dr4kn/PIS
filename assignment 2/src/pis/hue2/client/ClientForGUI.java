package pis.hue2.client;

import pis.hue2.fileSending.FileSender;

import java.io.*;
import java.net.*;
import java.net.Socket;

public class ClientForGUI {
    private static final int PORT = 42069;
    private static final String filePath = "testFiles/client/";
    private static String getFileName = "";
    private static String delFileName = "";
    private static String putFileName = "";
    private static String request;

    Socket socket = new Socket("localhost", PORT);

    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    public ClientForGUI() throws IOException {}

    public String runRequestByInput(String request) throws Exception
    {
        String answer = "";
        if (request.contains("GET"))
        {
            try
            {
                StringBuilder getFile = new StringBuilder();
                for (char ch : request.substring(4).toCharArray())
                {
                    getFile.append(ch);
                }
                getFileName = getFile.toString();
                request = request.split(" ")[0];
            }
            catch (StringIndexOutOfBoundsException e)
            {
                System.out.println("Must Specify the File in the Get Request");
            }
        }

        if (request.contains("DEL"))
        {
            try
            {
                StringBuilder delFile = new StringBuilder();
                for (char ch : request.substring(4).toCharArray())
                {
                    delFile.append(ch);
                }
                delFileName = delFile.toString();
                request = request.split(" ")[0];
            }
            catch (StringIndexOutOfBoundsException e)
            {
                System.out.println("Must Specify the File in the DEL Request");
            }
        }

        if (request.contains("PUT"))
        {
            try
            {
                StringBuilder putFile = new StringBuilder();
                for (char ch : request.substring(4).toCharArray())
                {
                    putFile.append(ch);
                }
                putFileName = putFile.toString();
                request = request.split(" ")[0];
            }
            catch (StringIndexOutOfBoundsException e)
            {
                System.out.println("Must Specify the File in the PUT Request");
            }
        }

        switch (request)
        {
            case "CON":
                out.println("CON");
                String serverResponseCON = input.readLine();
                if(serverResponseCON.equals("ACK")){
                    answer = "Server: " + "Your connection was accepted.";
                }
                else if(serverResponseCON.equals("DND")){
                    answer = "Server: " + "The server denied your request.";
                }
                else{
                    answer = "Server: " + "Didn't get your request.";
                }
                break;

            case "DEL":
                out.println("DEL " + delFileName);
                String serverResponseDEL = input.readLine();
                if(serverResponseDEL.equals("ACK")){
                    answer = "Server: " + "Your delete request was accepted.";
                    out.println("ACK");
                    try
                    {
                        FileSender fileSender = new FileSender(filePath, socket);
                        fileSender.deleteFile();
                        out.println("ACK");
                    }
                    catch(Exception e)
                    {
                        out.println("DND");
                        answer =  "File Transfer went wrong";
                    }
                }
                else if(serverResponseDEL.equals("DND")){
                    answer = "Server: " + "There was an error trying to delete the file from the server.";
                }
                else{
                    answer = "Server: " + "Didn't get your request.";
                }
                break;

            case "DSC":
                out.println("DSC");
                String serverResponseDSC = input.readLine();
                if(serverResponseDSC.equals("DSC")){
                    answer = "Server: " + "You were disconnected successfully.";
                }
                else{
                    answer = "Server: " + "There was an error while trying to disconnect you.";
                }
                break;

            case "GET":
                out.println("GET " + getFileName);
                String serverResponseGET = input.readLine();
                if(serverResponseGET.equals("ACK")){
                    answer = "Server: " + "Your get request was accepted.";
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
                        answer = "File Transfer went wrong";
                    }
                }
                else if(serverResponseGET.equals("DND")){
                    answer = "Server: " + "There was an error trying to get the file from the server.";
                }
                else{
                    answer = "Server: " + "Didn't get your request.";
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
                            allFiles.append(", ");
                        }
                        else
                        {
                            allFiles.append(ch);
                        }
                    }
                    answer = allFiles.toString();

                    out.println("ACK");
                }
                else
                {
                    answer = "Server: " + "There was an error while trying to list the data.";
                }
                break;

            case "PUT":
                out.println("PUT");
                String serverResponsePUT = input.readLine();

                if(serverResponsePUT.equals("ACK"))
                {
                    System.out.println("The File Transfer was successful");
                    FileSender fileSender = new FileSender(filePath + putFileName, socket);
                    fileSender.sendFile();
                }
                else if(serverResponsePUT.equals("DND"))
                {
                    answer = "File Transfer wasn't successful";
                }
                else
                {
                    answer =  "unknown command in put";
                }
                break;

            default:
                answer = "Server didn't respond or you used a non existing command.";
                break;

        }
        return answer;
    }



}
