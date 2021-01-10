package pis.hue2.fileSending;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileSender
{
    private String filepath;
    private Socket client;

    public FileSender(String filepath, Socket client)
    {
        this.filepath = filepath;
        this.client = client;
    }

    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
    }

    public String readFile()
    {
        File file = new File(filepath);

        try
        {
            Scanner scanner = new Scanner(file);
            String fileContent = "";

            while (scanner.hasNextLine())
            {
                fileContent += scanner.nextLine();
            }
            scanner.close();
            return fileContent;
        }
        catch (FileNotFoundException e)
        {
            return "404";
        }
    }

    public void receiveFile()
    {
        try
        {
            int bytesRead;

            DataInputStream clientData = new DataInputStream(client.getInputStream());

            String fileName = clientData.readUTF();

            File file = new File(filepath + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            if (!file.exists())
            {
                file.createNewFile();
            }

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

    public boolean deleteFile()
    {
        File file = new File(filepath);
        return file.delete();
    }
}
