package root.main;

import root.shop.login.Login;
import root.shop.login.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum Error {
    USERS_XML_MISSING,
    INVALID_USERNAME_OR_PASSWORD,
    USERNAME_NOT_ENTERED,
    PASSWORD_NOT_ENTERED,
    ROLE_ELEMENT_MISSING,
    ROLE_TOO_HIGH,
    ROLE_TOO_LOW,
    ACCOUNT_HAS_EXPIRED,
    ROLE_ELEMENT_ONE_LETTER_LONG,
    ROLE_ELEMENT_IS_CHARACTER,
    XML_SYNTAX_ERROR,
    DECRYPTION_ERROR,
    OTHER,
    TEMP_TEMP_MISSING;

    private static final String LOGGING_FILE_PATH = "C:\\ProgramData\\crackmelogs.txt";

    private static Login loginControllerInstance;
    public static void setLoginControllerInstance() {
        loginControllerInstance = Login.getLoginControllerInstance();
    }

    public static void processError(Error errorType, String message) {

        System.out.println("Processing error: " + message);

        switch (errorType) {
            case USERS_XML_MISSING:
                logError(USERS_XML_MISSING, message);
                loginControllerInstance.getErrorLabel().setText("users.xml is missing");
                break;

            case INVALID_USERNAME_OR_PASSWORD:
                loginControllerInstance.getErrorLabel().setText("Invalid username or password entered");
                break;

            case ROLE_ELEMENT_MISSING:
                logError(ROLE_ELEMENT_MISSING, message);
                loginControllerInstance.getErrorLabel().setText("<role> element is missing");
                break;

            case ROLE_TOO_HIGH:
                loginControllerInstance.getErrorLabel().setText("The role is too high for this user");
                break;

            case ROLE_TOO_LOW:
                loginControllerInstance.getErrorLabel().setText("The role is too low for this user");
                break;

            case ACCOUNT_HAS_EXPIRED:
                loginControllerInstance.getErrorLabel().setText("This account has expired");
                break;

            case ROLE_ELEMENT_ONE_LETTER_LONG:
                loginControllerInstance.getErrorLabel().setText("The role must be one character long");
                break;

            case ROLE_ELEMENT_IS_CHARACTER:
                loginControllerInstance.getErrorLabel().setText("The role must be a character");
                break;

            case XML_SYNTAX_ERROR:
                logError(XML_SYNTAX_ERROR, message);
                loginControllerInstance.getErrorLabel().setText("Syntax error in users.xml or exactly one root element, missing");
                User.getUsers().clear(); // We want the errors to be completely gone before checking all users.
                break;

            case DECRYPTION_ERROR:
                logError(DECRYPTION_ERROR, message);
                loginControllerInstance.getErrorLabel().setText("Decryption error has occurred");
                break;

            case USERNAME_NOT_ENTERED:
                loginControllerInstance.getErrorLabel().setText("Username field is empty");
                break;

            case PASSWORD_NOT_ENTERED:
                loginControllerInstance.getErrorLabel().setText("Password field is empty");
                break;
            case TEMP_TEMP_MISSING:
                logError(TEMP_TEMP_MISSING, message);
                loginControllerInstance.getErrorLabel().setText("temp.temp is missing, ask for help(no drill bruv)");
                break;
            case OTHER:
                logError(OTHER, message);
        }
    }

    private static void logError(Error type, String message) {

        String finalStringLog;
        StringBuilder fileContent = General.readFileAsString(LOGGING_FILE_PATH);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime presentTime = LocalDateTime.now();

        String loggingMessage = dateTimeFormatter.format(presentTime) + " - TYPE:" + type.toString() + "\nNOTE:" + message + "\n";
        fileContent.append(loggingMessage);

        finalStringLog = fileContent.toString();


        try(FileWriter fileWriter = new FileWriter(LOGGING_FILE_PATH)) {

            fileWriter.write(finalStringLog);

        } catch (IOException e) {
            // if fileWriter is null it should have returned already.
            System.out.println("Unexpected IOException regarding File Writer was thrown..");
            e.printStackTrace();
        }
    }

    public static void initializeLoggerFile() {

        try {
            File loggerFile = new File(LOGGING_FILE_PATH);

            if (!loggerFile.exists()) {
                loggerFile.createNewFile();
            }

        } catch(IOException e) {
            System.out.println("Unexpected IOException regarding logger file was thrown..");
            e.printStackTrace();
        }
    } // called at Main.main()

}
