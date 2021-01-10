package pis.hue2.client;


public class StartGUI extends Thread
{
    public void run()
    {
        try {
            ClientGUI clientGUI = new ClientGUI();
        } catch (Exception e) {
           System.out.println(e);
        }
    }
}
