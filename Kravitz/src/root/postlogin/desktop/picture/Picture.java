package root.postlogin.desktop.picture;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import root.main.Error;

import java.io.IOException;

public class Picture {

    private static Stage pictureWindow = null;
    public static Stage getPictureWindow() {
        return pictureWindow;
    }

    public static void displayPicture() {
        if (pictureWindow!=null && pictureWindow.isShowing()) {
            pictureWindow.requestFocus();
            return;
        }

        pictureWindow = new Stage();

        FXMLLoader loader = new FXMLLoader(Picture.class.getResource("Picture.fxml"));
        Parent pictureRoot = null;

        try {
            pictureRoot = loader.load();


        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in Picture.displayPictureWindow() at line " + e.getStackTrace()[0].getLineNumber());
        }

        pictureWindow.setScene(new Scene(pictureRoot, 900, 700));

        pictureWindow.setResizable(false);
        pictureWindow.setTitle("BAN HASHTAGS!!");
        pictureWindow.show();
    }

}
