package root.end;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import root.postlogin.PostLoginController;

import java.io.File;

public class End {

    private static boolean variable = false; // for purpose of switching between two cases
    private static float seconds = 0;
    private static Timeline timeTimeline;
    private static Timeline colorTimeline;

    public static void displayEnd() {
        Stage window = new Stage();

        StackPane stackPane = new StackPane();

        Label label = new Label();
        label.setFont(new Font(24));
        label.setStyle("-fx-background-color: yellow;");
        label.setStyle("-fx-border-color: black;");
        label.setStyle("-fx-text-fill: red;");

        stackPane.getChildren().add(label);

        Scene scene = new Scene(stackPane, 600, 400);
        window.setScene(scene);
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        window.show();

        AudioClip bombAudioClip = new AudioClip(PostLoginController.class.getResource("/resources/bomb.mp3").toString());
        bombAudioClip.play();

        colorTimeline = new Timeline(new KeyFrame(Duration.seconds(0.25),
                e-> {
                    label.setText(String.format("SELF DESTRUCTING IN %.2f", (4 - seconds)));
                    if (variable) {
                        stackPane.setStyle("-fx-background-color: red;");
                        variable=false;
                    } else {
                        stackPane.setStyle("-fx-background-color: yellow;");
                        variable=true;
                    }
                }));



        timeTimeline = new Timeline(new KeyFrame(Duration.seconds(0.05),
                e-> {
                    seconds+=0.05;
                    label.setText(String.format("SELF DESTRUCTING COMPUTER IN %.2f", (4 - seconds)));
        }));

        timeTimeline.setCycleCount(80);
        colorTimeline.setCycleCount(16);

        timeTimeline.play();
        colorTimeline.play();

        timeTimeline.setOnFinished(e -> {

            window.close();
            try {
                AudioClip explosionAudioClip = new AudioClip(PostLoginController.class.getResource("/resources/explosion.mp3").toString());
                explosionAudioClip.play();
                Thread.sleep(3000);
            } catch(Exception ignored) {

            }
            displayCongratulations();

        });
    }

    private static void displayCongratulations() {
        Stage window = new Stage();

        StackPane stackPane = new StackPane();

        Label label = new Label("Thank you for trying out my crackme!\nThis is really challenging so be proud of yourself :)\n\nA secret logfile located at C:\\ProgramData\\crackmelogs.txt has been deleted\nFiles at C:\\Users\\Public\\Document haven't been deleted");
        label.setFont(new Font(14));

        stackPane.getChildren().add(label);

        Scene scene = new Scene(stackPane, 700, 200);
        window.setScene(scene);
        window.setResizable(false);

        AudioClip congratsAudioClip = new AudioClip(PostLoginController.class.getResource("/resources/congrats.mp3").toString());
        congratsAudioClip.play();
        window.show();


        File crackmeLogs = new File("C:\\ProgramData\\crackmelogs.txt");
        crackmeLogs.delete();


    }

}
