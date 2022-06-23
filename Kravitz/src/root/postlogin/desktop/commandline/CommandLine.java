package root.postlogin.desktop.commandline;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import root.postlogin.PostLoginController;

public class CommandLine {

    private static Stage commandLineWindow;

    public static Stage getCommandLineWindow() {
        return commandLineWindow;
    }

    private static TextArea textArea = new TextArea("");

    public static void setTextAreaAsCommandLine() {

        textArea.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String[] splitText = textArea.getText().split("\n");
                String commandString = splitText[splitText.length - 1];

                textArea.clear();

                if (commandString.equals("help")) {

                    textArea.appendText("help - displays help\n");
                    textArea.appendText("attrib - changes attributes of a file\n");
                    textArea.appendText("dir - displays directory content\n");
                    textArea.appendText("exit - closes command prompt\n");

                } else if (commandString.startsWith("attrib")) {
                    if (commandString.contains("-h")) {
                        String[] splits = commandString.split(" ");

                        if (splits.length < 3 || splits.length > 4) {
                            textArea.appendText("Syntax error - too many attributes\n");
                        } else {
                            String name = "";
                            for (String split : splits) {
                                if (split.equals("-h") || split.contains("attrib")) {
                                    continue;
                                }
                                name = split;
                                break;
                            }

                            if (name.equalsIgnoreCase("Assembly_Runner")) {
                                showAssemblyRunner();
                                textArea.appendText("hidden attribute removed for file: \"" + name + "\"\n");
                            } else {
                                textArea.appendText("inappropriate file name: " + name + "\n");
                            }
                        }
                    } else {
                        textArea.appendText("Command syntax: attrib -(attribute type) (file name)\n");
                    }
                } else if (commandString.equals("dir")) {
                    textArea.appendText("cmd  \"My_Propaganda\"  \"Memory_Management_System\"  \"Assembly_Runner\"\n");
                } else if (commandString.equals("exit")) {
                    commandLineWindow.close();
                } else {
                    textArea.appendText("unknown command - try 'help'\n");
                }

            }
        });

        commandLineWindow = new Stage();

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(textArea);

        Scene scene = new Scene(stackPane, 600, 400);
        scene.getStylesheets().add("root/postlogin/desktop/commandline/cmd.css");

        commandLineWindow.initStyle(StageStyle.UNDECORATED);
        commandLineWindow.setScene(scene);
        commandLineWindow.show();
    }

    public static void showAssemblyRunner() {
        ImageView assemblyRunnerImage = PostLoginController.getPostLoginController().getAssemblyImage();
        assemblyRunnerImage.setDisable(false);
        assemblyRunnerImage.setVisible(true);
    }

}
