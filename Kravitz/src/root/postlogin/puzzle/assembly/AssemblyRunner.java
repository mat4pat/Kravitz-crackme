package root.postlogin.puzzle.assembly;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import root.end.End;
import root.main.Error;
import root.postlogin.PostLoginController;
import root.postlogin.puzzle.Binary;
import root.postlogin.puzzle.memory.MemoryTable;

import java.io.IOException;

public class AssemblyRunner {

    private static Stage assemblyRunnerWindow;
    public static Stage getAssemblyRunnerWindow() {
        return assemblyRunnerWindow;
    }

    private static AssemblyRunner assemblyRunnerController;
    public static AssemblyRunner getAssemblyRunnerController() { return assemblyRunnerController; }

    public static void displayAssemblyRunner() {
        if (assemblyRunnerWindow!=null && assemblyRunnerWindow.isShowing()) {
            assemblyRunnerWindow.requestFocus();
            return;
        }

        assemblyRunnerWindow = new Stage();

        FXMLLoader loader = new FXMLLoader(AssemblyRunner.class.getResource("AssemblyRunner.fxml"));
        Parent assemblyRunnerRoot = null;

        try {
            assemblyRunnerRoot = loader.load();
            assemblyRunnerController = loader.getController();

        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in AssemblyRunner.displayAssemblyRunnerWindow() at line " + e.getStackTrace()[0].getLineNumber());
        }

        assemblyRunnerWindow.setScene(new Scene(assemblyRunnerRoot));
        assemblyRunnerWindow.setResizable(false);
        assemblyRunnerWindow.setTitle("Assembly Runner");
        assemblyRunnerWindow.show();
    }

    private static int AX=0;

    @FXML
    private Label AX_Label;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField commandField;

    @FXML
    private void runCommand(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {

            errorLabel.setText("");
            Stage memoryTable = MemoryTable.getMemoryTableWindow();
            if (memoryTable==null) {
                errorLabel.setText("Use the memory management system!");
                return;
            }

            String command = commandField.getText();
            if (command.equals("2")) {
                Binary.pushAX(AX);
            }
            else if (command.equals("3")) {

                int valueOfFourthBinary = Binary.getValueOfFourthBinary();
                final int SECRET_CODE = 35;
                if (valueOfFourthBinary==SECRET_CODE) {

                    End.displayEnd();
                    PostLoginController.closeAllPostLoginWindows();
                } else {

                    errorLabel.setText("Wrong! MyPropoganda.exe didn't execute till the end!");
                }
            }
            else if (command.startsWith("1 ") && (command.length()>=3 && command.length()<=5)) {

                for (int index=2; index<command.length(); index++) {
                    if (!(command.charAt(index)>='0' && command.charAt(index)<='9')) { // if x isn't a number
                        errorLabel.setText("Syntax error");
                        return;
                    }
                }

                int x = Integer.parseInt(command.substring(2));
                if (x>=0 && x<=255) {
                    AX=x;
                    AX_Label.setText("AX=" + AX);
                } else {
                    errorLabel.setText("x cannot be equal to: " + x);
                }
            }
            else {
                errorLabel.setText("Syntax error");
            }


        }
    }
}
