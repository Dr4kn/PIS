package pis.hue2.client;

import pis.hue2.common.FileSender;

import java.io.*;
import java.net.Socket;

public class LaunchClient extends ClientGUI {
    private static final int PORT = 42069;
    private static final String filePath = "testFiles/client/";
    private static String fileName = "";

    /**
     * opens a socket on the given PORT
     * creates input and output to communicate with the server
     */
    Socket socket = new Socket("localhost", PORT);

    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    public LaunchClient() throws IOException {}

    /**
     * starts the ClientGUI
     * @param args arguments
     */
    public static void main(String[] args)
    {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.start();
    }


    /**
     * @param request CON, GET + data, DEL + data, PUT + data, DSC, LST are acceptable
     * @return this method returns the answer from the server from a given request.
     * @throws Exception if the request isn't properly written
     *
     */
    public String runRequestByInput(String request) throws Exception
    {
        String answer = "";
        /*
         *  if request is a GET, DEL or PUT we get the filename from this method
         */
        if (request.contains("GET") || request.contains("DEL") || request.contains("PUT"))
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

        /*
         * switch-case for all requests
         */
        switch (request)
        {
            /**
             * CON request
             * connects to the server and gives its answer
             */
            case "CON" -> {
                out.println("CON");
                String serverResponseCON = input.readLine();
                if (serverResponseCON.equals("ACK"))
                {
                    answer = "Server: " + "Your connection was accepted.";
                }
                else if (serverResponseCON.equals("DND"))
                {
                    answer = "Server: " + "The server denied your request.";
                }
                else
                {
                    answer = "Server: " + "Didn't get your request.";
                }
            }

            /*
             * DEL request
             * deletes a file with "fileName" on the server and gives its answer
             */
            case "DEL" -> {
                out.println("DEL " + fileName);
                fileName = "";
                String serverResponseDEL = input.readLine();
                if (serverResponseDEL.equals("ACK"))
                {
                    answer = "Server: " + "Your delete request was accepted.";
                    out.println("ACK");
                    try
                    {
                        FileSender fileSender = new FileSender(filePath, socket);
                        fileSender.deleteFile();
                        out.println("ACK");
                    }
                    catch (Exception e)
                    {
                        out.println("DND");
                        answer = "File Transfer went wrong";
                    }
                }
                else if (serverResponseDEL.equals("DND"))
                {
                    answer = "Server: " + "There was an error trying to delete the file from the server.";
                }
                else
                {
                    answer = "Server: " + "Didn't get your request.";
                }
            }

            /*
             * DSC request
             * disconnects this client and gives its answer
             */
            case "DSC" -> {
                out.println("DSC");
                String serverResponseDSC = input.readLine();
                if (serverResponseDSC.equals("DSC"))
                {
                    answer = "Server: " + "You were disconnected successfully.";
                }
                else
                {
                    answer = "Server: " + "There was an error while trying to disconnect you.";
                }
            }

            /*
             * GET request
             * gets data from the server with "fileName" and gives its answer
             */
            case "GET" -> {
                out.println("GET " + fileName);
                fileName = "";
                String serverResponseGET = input.readLine();
                if (serverResponseGET.equals("ACK"))
                {
                    answer = "Server: " + "Your get request was accepted.";
                    out.println("ACK");
                    if (input.readLine().equals("DAT"))
                    {
                        try
                        {
                            FileSender fileSender = new FileSender(filePath, socket);
                            fileSender.receiveFile();
                            out.println("ACK");
                        }
                        catch (Exception e)
                        {
                            out.println("DND");
                            answer = "File Transfer went wrong";
                        }
                    }
                }
                else if (serverResponseGET.equals("DND"))
                {
                    answer = "Server: " + "There was an error trying to get the file from the server.";
                }
                else
                {
                    answer = "Server: " + "Didn't get your request.";
                }
            }

            /*
             * LST request
             * lists all files on the server and gives its answer
             */
            case "LST" -> {
                out.println("LST");
                String serverResponseLST = input.readLine();
                if (serverResponseLST.equals("ACK"))
                {
                    out.println("ACK");

                    StringBuilder allFiles = new StringBuilder();
                    for (char ch : input.readLine().toCharArray())
                    {
                        if (ch == '|')
                        {
                            allFiles.append('\n');
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
            }

            /*
             * PUT request
             * puts a file on the server with "fileName" and gives its answer
             */
            case "PUT" -> {
                out.println("PUT");
                String serverResponsePUT = input.readLine();
                if (serverResponsePUT.equals("ACK"))
                {
                    out.println("DAT");
                    FileSender fileSender = new FileSender(filePath + fileName, socket);
                    fileName = "";
                    fileSender.sendFile();
                    System.out.println("The File Transfer was successful");
                }
                else if (serverResponsePUT.equals("DND"))
                {
                    answer = "File Transfer wasn't successful";
                }
                else
                {
                    answer = "unknown command in put";
                }
            }
            default -> answer = "Server didn't respond or you used a non existing command.";
        }

        return answer;
    }
}
