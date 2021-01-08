package pis.hue2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
{
    public Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public  ClientHandler(Socket clientSocket) throws IOException
    {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String request = in.readLine();
                instruction(request);
            }
        }
        catch (IOException e)
        {
            System.err.println("IO exception in client handler");
            System.err.println(e.getStackTrace());
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
        }
    }

    public void instruction(String instruction)
    {
        switch (instruction)
        {
            /**
             * CONNECT
             * connection request
             * usage: CON
             */
            case "CON":
                System.out.println("CON");
                // send ACK if success
                out.println("ACK");
                System.out.println("Client accepted");

                // send DND if unsuccessful
                break;

            /**
             * DISCONNECT
             * disconnect notification
             * usage: DSC
             */
            case "DSC":
                out.println("DSC");
                closeSocket();
                System.out.println("Client disconnected");
                break;

            /**
             * ACKNOWLEDGED
             * operation acknowledgement
             * usage: ACK
             */
            case "ACK":
                out.println("ACK");
                System.out.println("ACK");
                break;

            /**
             * DENIED
             * negative operation acknowledgement
             * usage: DND
             */
            case "DND":
                out.println("DND");
                System.out.println("DND: 3 Clients are already connected");
                break;

//            /**
//             * LIST
//             * list a directory
//             * usage: LST
//             **/
//            case "LST":
//                System.out.println("LST");
//                // send ACK
//                // wait for ACK
//                // send DAT
//                // wait for ACK
//                break;
//
//            /**
//             * UPLOAD
//             * upload a file
//             * usage: PUT <filename : string>
//             */
//            case "PUT":
//                System.out.println("PUT");
//                // send ACK
//                // wait for DAT
//                // send ACK
//                // send DND if unsuccessful
//                break;
//
//            /**
//             * DOWNLOAD
//             * download a file
//             * usage: GET <filename : string>
//             */
//            case "GET":
//                System.out.println("GET");
//                // send ACK
//                // wait for ACK
//                // send DAT
//                // wait for ACK
//                // send DND if unsuccessful
//                break;
//
//            /**
//             * DELETE
//             * delete a file
//             * usage: DEL <filename : string>
//             */
//            case "DEL":
//                System.out.println("DEL");
//                // send ACK if success
//                // send DND if unsuccessful
//                break;
//
//            /**
//             * DATA
//             * encapsulates the data to be transmitted
//             * usage: DAT <length : string (long)> <data : byte[]>
//             */
//            case "DAT":
//                System.out.println("DAT");
//                break;

            default:
                out.println("404");
                System.out.println(instruction);
                System.out.println("Error: Command not recognised");
                break;
        }
    }
}
