package pis.hue2.common;

import java.io.*;
import java.net.Socket;
//import java.util.Scanner;

/**
 * used for the client and server to send and read files
 * (GET | PUT | DEL)
 */
public class FileSender
{
    private final String filepath;
    private final Socket client;

    /**
     * @param filepath the filepath including the file name
     * @param client the socket that is connected to the other party
     */
    public FileSender(String filepath, Socket client)
    {
        this.filepath = filepath;
        this.client = client;
    }

    /**
     * used to accept a file
     * parameter should be set to your filepath only
     * the correct file name is added automatically
     */
    public void receiveFile()
    {
        try
        {
            int bytesRead;

            DataInputStream clientData = new DataInputStream(client.getInputStream());

            String fileName = clientData.readUTF();

            File file = new File(filepath + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

//            if (!file.exists())
//            {
//                file.createNewFile();
//            }

            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1)
            {
                fileOutputStream.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            fileOutputStream.flush();
            fileOutputStream.close();

            System.out.println("File " + fileName + " received");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("receiveFile IOException");
        }
    }

    /**
     * needs full filepath
     * and starts sending the file
     */
    public void sendFile()
    {
        try
        {
            File myFile = new File(filepath);  //handle file reading
            byte[] byteArray = new byte[(int) myFile.length()];

            FileInputStream fileInputStream = new FileInputStream(myFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
            dataInputStream.readFully(byteArray, 0, byteArray.length);

            //handle file send over socket
            OutputStream outputStream = client.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(myFile.getName());
            dataOutputStream.writeLong(byteArray.length);
            dataOutputStream.write(byteArray, 0, byteArray.length);
            dataOutputStream.flush();
            System.out.println("File " + filepath + " sent");
        }
        catch (Exception e)
        {
            System.err.println("File does not exist!");
        }
    }

    /**
     * @return true if file was deleted
     */
    public boolean deleteFile()
    {
        File file = new File(filepath);
        return file.delete();
    }
}
