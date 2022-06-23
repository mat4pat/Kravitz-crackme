package root.postlogin.desktop.closingscreen;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ClosingScreen {

    public static void displayClosingScreenWindow() {
        Stage closingWindow = new Stage();

        Label mainLabel = new Label();
        StackPane mainPane = new StackPane();

        mainLabel.setText("Please come during employee hours");
        mainLabel.setFont(new Font("Tahoma", 12));
        mainPane.getChildren().add(mainLabel);
        mainPane.setStyle("-fx-border-color: cornflowerblue;");

        Scene mainScene = new Scene(mainPane, 300, 150);

        closingWindow.setAlwaysOnTop(true);
        closingWindow.initStyle(StageStyle.UNDECORATED);
        closingWindow.initModality(Modality.APPLICATION_MODAL);

        closingWindow.setScene(mainScene);
        closingWindow.show();

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3)); // Wait a couple seconds
        pauseTransition.setOnFinished(e -> closingWindow.close()); // When the timer is over, close the window
        pauseTransition.play(); // Play the pause
    }

}
