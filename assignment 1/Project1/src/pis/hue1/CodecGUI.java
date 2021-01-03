package pis.hue1;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.*;


public class CodecGUI extends Application
{



    private ComboBox mode;
    private TextArea plainText;
    private TextArea cipherText;

    private TextField encryptionKey1;
    private TextField encryptionKey2;

    private Wuerfel cube = new Wuerfel();
    private Caesar caesar = new Caesar();


    private Button encode;
    private Button decode;
    private Button quit;

    private Stage window;

    /**
     * @param primaryStage automaticly starts the javafx interface
     */
    @Override
    public void start(Stage primaryStage) //throws Exception
    {
        this.window = primaryStage;

        /*
        all elements used
         */

        plainText = new TextArea();
        plainText.setPromptText("Plain Text");
        plainText.setPrefSize(300, 120);

        cipherText = new TextArea();
        cipherText.setPromptText("Cipher Text");
        cipherText.setPrefSize(300, 120);

        encryptionKey1 = new TextField();
        encryptionKey1.setPromptText("First Encryption Key");

        encryptionKey2 = new TextField();
        encryptionKey2.setPromptText("Second Encryption Key");

        encode = new Button("encode");
        encode.setOnAction(e -> buttonAction(encode));

        decode = new Button("decode");
        decode.setOnAction(e -> buttonAction(decode));

        quit = new Button("Quit the program");
        quit.setOnAction(e -> buttonAction(quit));

        ObservableList<String> options = FXCollections.observableArrayList(
                "Cube", "Caesar"
        );

        mode = new ComboBox(options);

        /*
        put the text fields on the same plain
         */

        HBox textBox = new HBox();
        textBox.setPadding(new Insets(0,10,10,10));
        textBox.setSpacing(10);
        textBox.getChildren().addAll(plainText, cipherText);

        /*
        encode and decode on same plain
         */

        HBox codeBox = new HBox();
        codeBox.setPadding(new Insets(0,10,10,10));
        codeBox.setSpacing(212);
        codeBox.getChildren().addAll(encode, decode);

        /*
        puts bose text fields below each other
         */

        VBox encryptionKeyBox = new VBox();
        encryptionKeyBox.setPadding(new Insets(0,10,10,10));
        encryptionKeyBox.setSpacing(6);
        encryptionKeyBox.getChildren().addAll(encryptionKey1, encryptionKey2);

        /*
        puts the buttons on the same plane
         */

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(0,10,10,10));
        bottomBox.setSpacing(184);
        bottomBox.getChildren().addAll(mode, quit);

        /*
        creates an object with all elements
         */

        VBox allBoxes= new VBox();
        allBoxes.setPadding(new Insets(10));
        allBoxes.setSpacing(8);
        allBoxes.getChildren().addAll(textBox, codeBox, encryptionKeyBox, bottomBox);

        /*
        creates an empty border around all elements
         */

        AnchorPane layout = new AnchorPane();
        layout.getChildren().add(allBoxes);
        layout.setBottomAnchor(allBoxes, 8.0);
        layout.setRightAnchor(allBoxes, 8.0);
        layout.setLeftAnchor(allBoxes, 8.0);
        layout.setTopAnchor(allBoxes, 14.0);

        window.setTitle("Basic Encryption");
        window.setScene(new Scene(layout, 600, 320));
        window.show();
    }


    /**
     * @param errorMessage putts an error box infront of the interace with the message given
     */
    private void error(String errorMessage)
    {
        /**
        Displays various types of errors in the GUI
         */

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(errorMessage);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }


    /**
     * @param event decides upon the event given what to do
     *              if you create a new event you might have to add it here
     */
    private void buttonAction(Button event)
    {
        /**
        closes the program
         */

        String encryptionPlaceholder;

        if (event == quit)
        {
            Stage window = (Stage) quit.getScene().getWindow();
            window.close();
        }

        if (mode.getValue() == null) {
            error("Choose an encryption method");
        }

        /*
        makes cube object and only works with two given encryption keys
         */

        else if (mode.getValue().toString() == mode.getItems().get(0))
        {
            try
            {

                if (event == encode)
                {
                    if (plainText.getText().length() == 0)
                    {
                        throw new IllegalArgumentException("No Text to encrypt found");
                    }
                    cube.setzeLosung(encryptionKey1.getText());

                    encryptionPlaceholder = cube.kodiere(plainText.getText());

                    cube.setzeLosung(encryptionKey2.getText());

                    cipherText.setText(cube.kodiere(encryptionPlaceholder));
                }
                else if (event == decode)
                {
                    if (cipherText.getText().length() == 0)
                    {
                        throw new IllegalArgumentException("No Text to decrypt found");
                    }
                    cube.setzeLosung(encryptionKey2.getText());

                    encryptionPlaceholder = cube.dekodiere(cipherText.getText());

                    cube.setzeLosung(encryptionKey1.getText());

                    plainText.setText(cube.dekodiere(encryptionPlaceholder));
                }
            }
            catch (IllegalArgumentException iae)
            {
                error(iae.getMessage());
            }

        }

        /*
        sets the first key on the Caesar Object and ignores the second key if it is given and gives no notice that it ignored it
         */

        else if (mode.getValue().toString() == mode.getItems().get(1))
        {
            try
            {
                caesar.setzeLosung(encryptionKey1.getText());

                if (event == encode)
                {
                    cipherText.setText(caesar.kodiere(plainText.getText()));
                }
                else if (event == decode)
                {
                    plainText.setText(caesar.dekodiere(cipherText.getText()));
                }
            }
            catch (IllegalArgumentException iae)
            {
                error(iae.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
