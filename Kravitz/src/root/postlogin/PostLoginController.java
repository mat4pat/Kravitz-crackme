package root.postlogin;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import root.main.Error;
import root.postlogin.desktop.closingscreen.ClosingScreen;
import root.postlogin.desktop.commandline.CommandLine;
import root.postlogin.desktop.picture.Picture;
import root.postlogin.desktop.shutdown.Shutdown;
import root.postlogin.puzzle.assembly.AssemblyRunner;
import root.postlogin.puzzle.memory.MemoryTable;

import java.io.IOException;
import java.time.LocalTime;

public class PostLoginController {

    private static Stage postLoginWindow;
    public static Stage getPostLoginWindow() {
        return postLoginWindow;
    }

    private static PostLoginController postLoginController;
    public static PostLoginController getPostLoginController() { return postLoginController; }

    public static void displayPostLoginWindow() {
        if (postLoginWindow!=null && postLoginWindow.isShowing()) {
            postLoginWindow.requestFocus();
            return;
        }

        postLoginWindow = new Stage();

        FXMLLoader loader = new FXMLLoader(PostLoginController.class.getResource("PostLogin.fxml"));
        Parent postLoginRoot = null;

        try {
            postLoginRoot = loader.load();
            postLoginController = loader.getController();

        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in PostLoginController.displayPostLoginWindow() at line " + e.getStackTrace()[0].getLineNumber());
        }

        postLoginWindow.setScene(new Scene(postLoginRoot, 1000, 560));
        postLoginWindow.initStyle(StageStyle.UNDECORATED);

        startTimer();
        playWindowsStartup(); // Acts like daemon thread, plays in the background.

    }

    private static void playWindowsStartup() {
        if (postLoginWindow.isShowing()) {
            AudioClip windowsXPAudioClip = new AudioClip(PostLoginController.class.getResource("/resources/WindowsStartup.mp3").toString());
            windowsXPAudioClip.play();
        }
        else {
            // don't play sound effect
        }

    }


    @FXML
    private Label timeLabel;

    @FXML
    private ImageView assemblyImage;
    public ImageView getAssemblyImage() { return assemblyImage; }

    @FXML
    public void assemblyRunnerImageClicked(MouseEvent e) { AssemblyRunner.displayAssemblyRunner(); }

    @FXML
    public void memoryPrintImageClicked(MouseEvent e) { MemoryTable.displayMemoryTable(); }

    @FXML
    public void commandLineImageClicked(MouseEvent e) { CommandLine.setTextAreaAsCommandLine(); }

    @FXML
    public void pictureFileImageClicked(MouseEvent e) { Picture.displayPicture(); }

    @FXML
    public void startButtonClicked(ActionEvent e) { Shutdown.displayShutdownWindow(); }

    private static int tSeconds = LocalTime.now().getSecond();
    private static Timeline timeline;

    private static void startTimer() {

        /* First section: check if the time is correct before continuing */

        int hours = LocalTime.now().getHour();

        if (!(hours<=6 && hours>=5)) {
            ClosingScreen.displayClosingScreenWindow();
            return;
        } else {
            postLoginWindow.show();
        }

        /* Display time in desktop */

        int minutes = LocalTime.now().getMinute();
        displayTimeOnDesktop(hours, minutes);


        /* Start timeline to check whether time is incorrect */
        tSeconds = LocalTime.now().getSecond();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                e-> {
                    tSeconds++;

                    if (tSeconds==60) { // Checks the condition each minute
                        tSeconds=0;

                        // Redisplay the time in desktop
                        int tHours = LocalTime.now().getHour();
                        int tMinutes = LocalTime.now().getMinute();
                        displayTimeOnDesktop(tHours, tMinutes);

                        // If time on computer isn't between 5 and 7 AM:
                        if ((tHours<5 || tHours>=7)) {
                            if (!postLoginWindow.isShowing()) {
                                timeline.stop(); // If desktop has been shutdown
                            } else { // If the desktop is still turned on.
                                closeAllPostLoginWindows();
                                ClosingScreen.displayClosingScreenWindow();
                                timeline.stop();
                            }
                        }
                    }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private static void displayTimeOnDesktop(int hours, int minutes) {
        if (minutes>=0 && minutes<=9) {
            postLoginController.timeLabel.setText(hours + ":0" + minutes);
        } else {
            postLoginController.timeLabel.setText(hours + ":" + minutes);
        }
    }

    public static void closeAllPostLoginWindows() {

        Stage postLoginWindow = getPostLoginWindow();
        Stage shutdownWindow = Shutdown.getShutdownWindow();
        Stage commandLineWindow = CommandLine.getCommandLineWindow();
        Stage pictureWindow = Picture.getPictureWindow();
        Stage memoryTableWindow = MemoryTable.getMemoryTableWindow();
        Stage assemblyRunnerWindow = AssemblyRunner.getAssemblyRunnerWindow();

        if (postLoginWindow.isShowing()) {
            postLoginWindow.close();
        }
        if (shutdownWindow!=null && shutdownWindow.isShowing()) {
            shutdownWindow.close();
        }
        if (commandLineWindow!=null && commandLineWindow.isShowing()) {
            commandLineWindow.close();
        }
        if (pictureWindow!=null && pictureWindow.isShowing()) {
            pictureWindow.close();
        }
        if (memoryTableWindow!=null && memoryTableWindow.isShowing()) {
            memoryTableWindow.close();
        }
        if (assemblyRunnerWindow!=null && assemblyRunnerWindow.isShowing()) {
            assemblyRunnerWindow.close();
        }
    }

}

