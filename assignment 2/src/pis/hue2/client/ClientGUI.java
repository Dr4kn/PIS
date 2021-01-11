package pis.hue2.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientGUI extends Thread
{
    private JTextField InputField;
    private JButton sendInputField;
    private JPanel ClientGUI;
    private JTextArea OutputTextArea;

    public ClientGUI()
    {
        sendInputField.addActionListener(new sendInputButtonClicked());
    }

    /**
     * runs the GUI of client (Thread)
     */
    public void run()
    {
        JFrame frame = new JFrame("ClientGUI");
        frame.setContentPane(ClientGUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * creates a LaunchClient
     */
    LaunchClient client;
    {
        try
        {
            client = new LaunchClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * uses LaunchClient to display the servers answer in the OutputTextArea
     */
    private class sendInputButtonClicked implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                OutputTextArea.setText(client.runRequestByInput(InputField.getText()));
            }
            catch (Exception exception)
            {
               System.out.println("Error");
            }
        }
    }

}
