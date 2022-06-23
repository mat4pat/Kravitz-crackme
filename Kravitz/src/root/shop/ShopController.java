package root.shop;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import root.shop.login.Login;
import root.main.Error;
import root.shop.cart.CartWindowController;

import java.io.IOException;

public class ShopController extends Application {

    private static Stage shopWindow;
    public static Stage getShopWindow() { return shopWindow; }

    @Override
    public void start(Stage primaryStage) {

        shopWindow = primaryStage;
        FXMLLoader loader = new FXMLLoader(ShopController.class.getResource("Shop.fxml"));
        Parent shopRoot = null;

        try {
            shopRoot = loader.load();

        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in Main.start() at line " + e.getStackTrace()[0].getLineNumber());
        }

        primaryStage.setTitle("Kavitz");
        primaryStage.setScene(new Scene(shopRoot));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Error.initializeLoggerFile();
        launch(args);
    }

    @Override
    public void stop() throws Exception {

        System.out.println("Closing..");
        super.stop();
    }


    @FXML
    public void LoginIconClicked(MouseEvent e) {
        Login.displayLoginWindow();
    }

    @FXML
    public void cartIconClicked(MouseEvent e) {
        CartWindowController.displayCartWindow();
    }
}
