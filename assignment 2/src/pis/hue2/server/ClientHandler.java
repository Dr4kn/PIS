package pis.hue2.server;

import pis.hue2.common.FileSender;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * The client talks to this part and server must recognise its commands
 * acceptable commands would be:
 * CON | connect
 * DSC | disconnect
 * LST | list Files
 * PUT | upload File
 * GET | download File
 * DAT | sends bytes
 */
public class ClientHandler implements Runnable
{
    public Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean accepted = false;
    private String fileName = "";
    String filePath = "testFiles/server/";

    /**
     * @param clientSocket a client that is already connected to one
     */
    public  ClientHandler(Socket clientSocket)
    {
        try
        {
            this.client = clientSocket;
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch (IOException e)
        {
            System.out.println("IO Exception in Client Handler constructor");
        }
    }

    /**
     * starts the Thread operation
     * client must first connect with CON to send the other commands
     * the other commands are also handled in here
     */
    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String request = in.readLine();
                /*
                 * CONNECT
                 * connection request
                 * usage: CON
                 */
                if (request.equals("CON") && !accepted)
                {
                    if (LaunchServer.getNumberOfClients() < 3)
                    {
                        out.println("ACK");
                        LaunchServer.increaseNumberOfClients();
                        accepted = true;
                        System.out.println("Client accepted");
                        System.out.println("Number of Clients: " + LaunchServer.getNumberOfClients());
                    }
                    else
                    {
                        out.println("DND");
                        LaunchServer.decreaseNumberOfClients();
                        System.out.println("DND: 3 Clients are already connected");
                        closeSocket();
                        System.out.println("Client disconnected");
                    }
                }
                else if (accepted)
                {
                    if (request.contains("GET") || request.contains("DEL"))
                    {
                        try
                        {
                            fileName = request.substring(4, request.length());
                            request = request.substring(0, 3);
                        }
                        catch (StringIndexOutOfBoundsException e)
                        {
                            System.out.println("Must Specify the File in the Get Request");
                        }
                    }
                    instruction(request);
                }
                else
                {
                    out.println("404");
                }
            }
        }
        catch (IOException e)
        {
            LaunchServer.decreaseNumberOfClients();
            System.out.println("Test");
            System.err.println("IO exception in client handler");
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        finally
        {
            out.close();
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("IOException in run");
            }
        }
    }

    /**
     * closes the socket when 3 clients are already connected
     * or a DSC is send
     */
    public void closeSocket()
    {
        out.close();
        try
        {
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("IOException in closeSocket");
        }
    }

    /**
     * @param instruction DSC, ACK, LST, PUT, GET, DAT are acceptable
     */
    public void instruction(String instruction)
    {
        switch (instruction)
        {
            case "DSC" -> {
                disconnect();
            }
            case "ACK" -> {
                acknowledge();
            }
            case "LST" -> {
                listFiles();
            }
            case "PUT" -> {
                sendFileToServer();
            }
            case "GET" -> {
                serverSendsFileToClient();
            }
            case "DEL" -> {
                deleteFile();
            }
            default -> {
                out.println("404");
                System.out.print(instruction);
                System.out.println(" | Error: Command not recognised");
            }
        }
    }

    /**
     * DISCONNECT
     * disconnect notification
     * usage: DSC
     */
    private void disconnect()
    {
        out.println("DSC");
        closeSocket();
        LaunchServer.decreaseNumberOfClients();
        System.out.println("Client disconnected");
    }

    /**
     * ACKNOWLEDGED
     * operation acknowledgement
     * usage: ACK
     */
    private void acknowledge()
    {
        out.println("ACK");
        System.out.println("ACK");
    }

    /**
     * LIST
     * list a directory
     * usage: LST
     **/
    private void listFiles()
    {
        out.println("ACK");
        try
        {
            if (in.readLine().equals("ACK"))
            {
                File file = new File(filePath);
                String[] pathNames = file.list();

                StringBuilder pathName = new StringBuilder();
                assert pathNames != null;
                for (String name : pathNames)
                {
                    pathName.append(name).append("|");
                }

                pathName = new StringBuilder(pathName.length() == 0 ? pathName.toString() : pathName.substring(0, pathName.length() - 1));

                out.println(pathName);
                if (in.readLine().equals("ACK"))
                {
                    System.out.println("Send List of all Files");
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("IOException in LST");
        }
    }

    /**
     * UPLOAD
     * upload a file
     * usage: PUT <filename : string>
     */
    private void sendFileToServer()
    {
        out.println("ACK");
        try
        {
            if (in.readLine().equals("DAT"))
            {
                FileSender fileSender = new FileSender(filePath, client);
                fileSender.receiveFile();
                out.println("ACK");
            }
        }
        catch (Exception e)
        {
            out.println("DND");
            System.out.println("File Transfer went wrong");
        }
    }

    /**
     * DOWNLOAD
     * download a file
     * usage: GET <filename : string>
     */
    public void serverSendsFileToClient()
    {
        out.println("ACK");
        try
        {
            if (in.readLine().equals("ACK"))
            {
                out.println("DAT");
                FileSender fileSender = new FileSender(filePath + fileName, client);
                fileName = "";
                fileSender.sendFile();
            }

            if (in.readLine().equals("ACK"))
            {
                System.out.println("The File Transfer was successful");
            }
            else if (in.readLine().equals("DND"))
            {
                System.out.println("File Transfer wasn't successful");
            }
            else
            {
                System.out.println("unknown command in put");
            }
        }
        catch (IOException e)
        {
            System.out.println("IOException in GET");
        }
    }

    /**
     * DELETE
     * delete a file
     * usage: DEL <filename : string>
     */
    public void deleteFile()
    {
        FileSender fileSender = new FileSender(filePath + fileName, client);
        fileName = "";
        if (fileSender.deleteFile())
        {
            out.println("ACK");
        }
        else
        {
            out.println("DND");
            System.out.println("Server full Client disconnected");
        }
    }
}
