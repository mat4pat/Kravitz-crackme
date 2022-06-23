package root.shop.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import root.main.Error;
import root.postlogin.PostLoginController;
import root.shop.ShopController;

import java.io.IOException;

public class Login {

    @FXML
    private TextField usernameField;

    public TextField getUsernameField() {
        return usernameField;
    }

    @FXML
    private TextField passwordField;

    public TextField getPasswordField() {
        return passwordField;
    }

    @FXML
    private Label errorLabel;

    public Label getErrorLabel() {
        return errorLabel;
    }

    private static Login loginControllerInstance;
    private static Stage loginWindow;


    public static void displayLoginWindow() {

        if (loginWindow!=null && loginWindow.isShowing()) {
            loginWindow.requestFocus();
            return;
        }

        loginWindow = new Stage();
        loginWindow.setTitle("Login");

        FXMLLoader loader = new FXMLLoader(Login.class.getResource("Login.fxml"));
        Parent loginRoot = null;

        try {
            loginRoot = loader.load();
            loginControllerInstance = loader.getController();

            Error.setLoginControllerInstance();

        } catch (IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in Login.displayLoginWindow() at line " + e.getStackTrace()[0].getLineNumber());
        }

        loginWindow.setScene(new Scene(loginRoot, 600, 400));
        loginWindow.show();

    }

    public static Login getLoginControllerInstance() {
        return Login.loginControllerInstance;
    }

    @FXML
    private void loginButtonClicked(ActionEvent e) {
        String usernameInput = getUsernameField().getText();
        String passwordInput = getPasswordField().getText();

        if (usernameInput.equals("")) {
            Error.processError(Error.USERNAME_NOT_ENTERED, "Username is empty");
            return;
        }
        else if (passwordInput.equals("")) {
            Error.processError(Error.PASSWORD_NOT_ENTERED, "Password is empty");
            return;
        }

        boolean result = User.compareUsers(usernameInput, passwordInput);
        if (result) {
            accessGranted();
        }

    }

    private static void accessGranted() {
        loginControllerInstance.getErrorLabel().setText("");
        loginControllerInstance.usernameField.clear();
        loginControllerInstance.passwordField.clear();

        loginWindow.close();
        ShopController.getShopWindow().close();

        PostLoginController.displayPostLoginWindow();
    }

}
