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
                if (request.contains("test"))
                {
                    out.println("test was successful");
                }
                else
                {
                    out.println("You typed: " + request);
                }
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
}
