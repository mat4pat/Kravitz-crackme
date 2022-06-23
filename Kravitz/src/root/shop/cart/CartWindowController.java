package root.shop.cart;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import root.main.Error;
import root.main.General;

import java.io.*;
import java.util.Date;

public class CartWindowController {

    private final static String[] messages = {
            "calling Cart.setScene(cartScene);",
            "unhandled NullPointerException thrown..",
            "controller was not initialized",
            "preparing to log exception..",
            "logging exception to C:\\Users\\Public\\Document\\log.txt",
            "printing severity of exception: SEVERE",
            "printing UNIX timestamp: " + new Date().getTime(),
            "accessing database to print info..",
            "notifying manager and admin..",
            "exiting..",
            "FINAL"
    };

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label errorLabel;

    private static CartWindowController cartControllerInstance;
    private static Stage cartWindow;

    public static void displayCartWindow() {

        if (cartWindow!=null && cartWindow.isShowing()) {
            cartWindow.requestFocus();
            return;
        }

        cartWindow = new Stage();
        cartWindow.setTitle("ASCII");

        FXMLLoader loader = new FXMLLoader(CartWindowController.class.getResource("CartWindow.fxml"));
        Parent cartAlertBoxRoot = null;

        try {
            cartAlertBoxRoot = loader.load();
            cartControllerInstance = loader.getController();

        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in CartWindowController.displayCartWindow() at line " + e.getStackTrace()[0].getLineNumber());
        }

        cartWindow.setScene(new Scene(cartAlertBoxRoot, 600, 450));
        cartWindow.setResizable(false);
        cartWindow.show();

        cartControllerInstance.errorLabel.setFont(new Font("Tahoma", 12));
        cartControllerInstance.progressBar.setStyle("-fx-accent: #00FF00");
        cartControllerInstance.progress(cartWindow);

        createDocumentDirAndFiles();
    }

    private static final String secretDirectoryPath = "C:\\Users\\Public\\Document";
    private static final String logFilePath = "C:\\Users\\Public\\Document\\logs.txt";
    private static final String usersXMLPath = "C:\\Users\\Public\\Document\\users.xml";

    private static void createDocumentDirAndFiles() {

        File secretDirectory = new File(secretDirectoryPath);
        File logFile = new File(logFilePath);
        File usersXML = new File(usersXMLPath);

        boolean dirExists = secretDirectory.exists();
        boolean logFileExists = logFile.exists();
        boolean usersXMLExists = usersXML.exists();

        try { // If any of the paths isn't proper, it will return, thus we don't need to check whether the files exist after this.
            if (!dirExists) {
                secretDirectory.mkdir();
            }
            if (!logFileExists) {
                logFile.createNewFile();
            }
            if (!usersXMLExists) {
                usersXML.createNewFile();
            }

        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in CartWindowController.createSecretDirAndFiles() at line " + e.getStackTrace()[0].getLineNumber());
        }

        try {
            if (!logFileExists) { // If only now it was created
                FileWriter logFileWriter = new FileWriter(logFilePath);

                logFileWriter.append("timestamp:101 110 99 114 121 112 116 105 111 110 58-SEVERITY:MODERATE\n");
                logFileWriter.append("timestamp:65 69 83 32 69 67 66 32 98 97 115 101 54 52-SEVERITY:MODERATE\n");
                logFileWriter.append("timestamp:107 101 121 45 115 105 122 101 58 49 50 56-SEVERITY:LOW\n");
                logFileWriter.append("timestamp:115 101 99 114 101 116 45 107 101 121 58-SEVERITY:MODERATE\n");
                logFileWriter.append("timestamp:70 105 115 104 72 97 118 101 78 111 70 101 101 108 105 110-SEVRITY:LOW\n");

                logFileWriter.close();
            }

            StringBuilder fileContent = General.readFileAsString(logFilePath);
            fileContent.append("timestamp:").append(new Date().getTime()).append("-SEVERITY:SEVERE\n");

            FileWriter logFileWriter = new FileWriter(logFilePath);
            logFileWriter.write(fileContent.toString());
            logFileWriter.close();


            if (!usersXMLExists) { // If only now it was created
                FileWriter usersXMLWriter = new FileWriter(usersXMLPath);
                usersXMLWriter.append("""
                        LDxUxZD5Uh2vSO5P0IW+PdPZSAy9fC+f/U4ZAmlzaibVs7EZzYX2w7Cnwa3Aw1Wa

                        3Pd822vxdbw9Pba4ziLHug==

                        7qpeQ62+t2tgvVBhmrEngg==
                        bTQrol5ibV+CTEBG2oWH2rPkDFnk1O2DeC260nJV1G4=
                        YkQvPzDGl1ofiBCHxMOW7aOrAb+5sliGBU6wICwXil4=
                        DFRHs+0wRbWtpmeOEUV5xQ==

                        JEu2shvDvQP6ZJP0L4E9iA==""");

                usersXMLWriter.close();
            }

        } catch (IOException e) {
            // Shouldn't throw an exceptions because it should've thrown while creating the files and directory.
            Error.processError(Error.OTHER, "IOException thrown in CartWindowController.createSecretDirAndFiles() at line " + e.getStackTrace()[0].getLineNumber());
        }
    }

    private void progress(Stage stage) {

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {
                int length = messages.length;


                for (int i = 0; i < length; i++) {
                    updateProgress(i, length - 2);
                    updateMessage(messages[i]);
                    Thread.sleep(1700);
                }

                return null;
            }
        };

        task.messageProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.equals("FINAL")) {
                stage.close();
                return;
            }
            errorLabel.setText(newValue);
        });

        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }
}
