package root.postlogin.desktop.shutdown;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import root.main.Error;
import root.postlogin.PostLoginController;

import java.io.IOException;

public class Shutdown {

    private static Stage shutdownWindow;
    public static Stage getShutdownWindow() { return shutdownWindow; }

    public static void displayShutdownWindow() {
        shutdownWindow = new Stage();

        FXMLLoader loader = new FXMLLoader(Shutdown.class.getResource("Shutdown.fxml"));
        Parent shutdownRoot = null;

        try {
            shutdownRoot = loader.load();
        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in Shutdown.displayShutdownWindow() at line " + e.getStackTrace()[0].getLineNumber());
        }

        Scene shutdownScene = new Scene(shutdownRoot, 400, 200, Color.BLUE);
        shutdownWindow.setScene(shutdownScene);

        shutdownWindow.setAlwaysOnTop(true);
        shutdownWindow.initModality(Modality.APPLICATION_MODAL);
        shutdownWindow.initStyle(StageStyle.UNDECORATED);

        shutdownWindow.show();
    }

    @FXML
    public void cancelButtonClicked(ActionEvent e) { shutdownWindow.close(); }

    @FXML
    public void shutdownButtonClicked(ActionEvent e) { PostLoginController.closeAllPostLoginWindows(); }
}
