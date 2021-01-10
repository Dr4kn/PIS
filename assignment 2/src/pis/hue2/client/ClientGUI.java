package pis.hue2.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {
    private JTextField InputField;
    private JTextField OutputField;
    private JButton sendInputField;
    private JPanel ClientGUI;
    ClientForGUI client = new ClientForGUI();

    public ClientGUI() throws Exception
    {
        sendInputField.addActionListener(new sendInputButtonClicked());
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("ClientGUI");
        frame.setContentPane(new ClientGUI().ClientGUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private class sendInputButtonClicked implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                OutputField.setText(client.runRequestByInput(InputField.getText()));
            }
            catch (Exception exception)
            {
               System.out.println("Error");
            }
        }
    }



}
