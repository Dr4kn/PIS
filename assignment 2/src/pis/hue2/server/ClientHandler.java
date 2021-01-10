package pis.hue2.server;

import pis.hue2.fileSending.FileSender;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable
{
    public Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean accepted = false;
    private final String filePath = "testFiles/server/";
    private String getFileName = "";
    private String delFileName = "";

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

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String request = in.readLine();
                if (!accepted)
                {
                    if (request.equals("CON"))
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
                    else
                    {
                        instruction("404");
                    }
                }
                else
                {
                    if (request.contains("GET"))
                    {
                        StringBuilder getFile = new StringBuilder();
                        for (char ch : request.substring(4).toCharArray())
                        {
                            getFile.append(ch);
                        }
                        getFileName = getFile.toString();
                        instruction("GET");
                    }

                    else if (request.contains("DEL"))
                    {
                        StringBuilder delFile = new StringBuilder();
                        for (char ch : request.substring(4).toCharArray())
                        {
                            delFile.append(ch);
                        }
                        delFileName = delFile.toString();
                        instruction("DEL");
                    }
                    else
                    {
                        instruction(request);
                    }
                }
            }
        }
        catch (IOException e)
        {
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

    public void instruction(String instruction)
    {
        switch (instruction)
        {
            /*
             * DISCONNECT
             * disconnect notification
             * usage: DSC
             */
            case "DSC":
                out.println("DSC");
                closeSocket();
                LaunchServer.decreaseNumberOfClients();
                System.out.println("Client disconnected");
                break;

            /*
             * ACKNOWLEDGED
             * operation acknowledgement
             * usage: ACK
             */
            case "ACK":
                out.println("ACK");
                System.out.println("ACK");
                break;

            /**
             * LIST
             * list a directory
             * usage: LST
             **/
            case "LST":
                out.println("ACK");
                try
                {
                    if(in.readLine().equals("ACK"))
                    {
                        File file = new File(filePath);
                        String[] pathNames = file.list();

                        String pathName = "";
                        for(int i = 0; i < pathNames.length; i++)
                        {
                            pathName += pathNames[i] + "|";
                        }

                        out.println(pathName);
                        if(in.readLine().equals("ACK"))
                        {
                            System.out.println("Send List of all Files");
                        }
                    }
                }
                catch (IOException e)
                {
                    System.out.println("IOException in LST");
                }

                // send DAT
                // wait for ACK
                break;

            /**
             * UPLOAD
             * upload a file
             * usage: PUT <filename : string>
             */
            case "PUT":
                out.println("ACK");
                try
                {
                    FileSender fileSender = new FileSender(filePath, client);
                    fileSender.receiveFile();
                    out.println("ACK");
                }
                catch(Exception e)
                {
                    out.println("DND");
                    System.out.println("File Transfer went wrong");
                }


                // send ACK
                // wait for DAT
                // send ACK
                // send DND if unsuccessful
                break;

            /**
             * DOWNLOAD
             * download a file
             * usage: GET <filename : string>
             */
            case "GET":
                out.println("ACK");

                try
                {
                    if(in.readLine().equals("ACK"))
                    {
                        FileSender fileSender = new FileSender(filePath + getFileName, client);
                        fileSender.sendFile();
                    }

                    if(in.readLine().equals("ACK"))
                    {
                        System.out.println("The File Transfer was successful");
                    }
                    else if(in.readLine().equals("DND"))
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

                break;

            /**
             * DELETE
             * delete a file
             * usage: DEL <filename : string>
             */
            case "DEL":
                FileSender fileSender = new FileSender(filePath + delFileName, client);
                if(fileSender.deleteFile())
                {
                    out.println("ACK");
                }
                else
                {
                    out.println("DND");
                }

//                delFileName
                // send ACK if success
                // send DND if unsuccessful
//                out.println("DND");
                break;

            /**
             * DATA
             * encapsulates the data to be transmitted
             * usage: DAT <length : string (long)> <data : byte[]>
//             */
            case "DAT":
                System.out.println("DAT");

                out.println("DAT");
                break;

            default:
                out.println("404");
                System.out.print(instruction);
                System.out.println(" | Error: Command not recognised");
                break;
        }
    }
}
