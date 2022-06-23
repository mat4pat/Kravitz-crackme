package root.shop.login;

import root.shop.login.encryption.AES;
import root.shop.login.encryption.DecryptionException;
import root.main.Error;

import java.io.*;
import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private String role;

    protected User(String username, String password, String role) { // Called only from XMLReader.java
        this.username=username;
        this.password=password;
        this.role=role;
    }

    private static final ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    /* DECRYPTION OF USERS.XML */

    private final static AES aes = AES.getInstance();

    private static final String TEMP_FILE_PATH = "C:\\Users\\Public\\temp.temp";
    private static final String FILE_PATH = "C:\\Users\\Public\\Document\\users.xml";

    enum STATUS {
        EXCEPTION_THROWN,
        ALL_WELL
    }

    private static STATUS decryptAndAddUsers() {

        /*
            This section is responsible for dealing with decryption
         */

        StringBuilder decryptedString = new StringBuilder();
        String encryptedLine;
        String decryptedLine;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            while ((encryptedLine = br.readLine()) != null) {

                try {
                    decryptedLine = aes.decrypt(encryptedLine);

                } catch(DecryptionException e) {
                    Error.processError(Error.DECRYPTION_ERROR, "An exception has occurred while decrypting the following line: " + encryptedLine);
                    return STATUS.EXCEPTION_THROWN;

                }

                decryptedString.append(decryptedLine).append("\n");
            }
        } catch(FileNotFoundException e) {
            Error.processError(Error.USERS_XML_MISSING, "Users.xml is not found");
            return STATUS.EXCEPTION_THROWN;

        } catch(IOException e){
            Error.processError(Error.OTHER, "IOException thrown in decryptAndAddUsers() at line " + e.getStackTrace()[0].getLineNumber());
            return STATUS.EXCEPTION_THROWN;

        }

        /*
            This section is responsible for writing to tempFile
         */

        File tempFile = new File(TEMP_FILE_PATH);
        if (tempFile.exists()) {
            tempFile.delete();
        }

        try {
            tempFile.createNewFile();
        } catch(IOException e) {

            Error.processError(Error.OTHER, "IOException thrown in decryptAndAddUsers() at line " + e.getStackTrace()[0].getLineNumber());
            tempFile.delete();

            return STATUS.EXCEPTION_THROWN;
        }

        try (FileWriter fileWriter = new FileWriter(TEMP_FILE_PATH)) {
            fileWriter.write(decryptedString.toString());
        } catch(IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in decryptAndAddUsers() at line " + e.getStackTrace()[0].getLineNumber());
            tempFile.delete();

            return STATUS.EXCEPTION_THROWN;

        }

        /*
            This section is responsible for retrieving data from the now decrypted Users.XML A.K.A temp.temp.
         */

        STATUS status = XMLReader.getUsersFromUsersXML(); // Uses tempFile too.

        tempFile.delete(); // If no exceptions have be thrown yet, delete only now tempFile, because XMLReader uses it too.

        if (status.equals(STATUS.EXCEPTION_THROWN)) {
            return STATUS.EXCEPTION_THROWN;
        }

        /*
            Returning if all absolutely no exceptions were no thrown.
         */

        return STATUS.ALL_WELL;
    }

    public static boolean compareUsers(String usernameInput, String passwordInput) {

        STATUS status = decryptAndAddUsers();
        if (status==STATUS.EXCEPTION_THROWN) {
            return false;
        }

        for (User user: users) { // Checking all the decrypted results with the inputs.

            String username = user.username;
            String password = user.password;
            String role = user.role;

            if (usernameInput.equals(username) && passwordInput.equals(password)) {

                if (username.equals("admin") && password.equals("incel4life")) { // trying to login into default given user.
                    Error.processError(Error.ACCOUNT_HAS_EXPIRED, "Account has expired.");
                    return false;
                }
                if (role==null) { // This is a special, only will run after we try to login into a user without a role!
                    Error.processError(Error.ROLE_ELEMENT_MISSING, "The user has tried to log into a user without a role.");
                    return false;
                }
                if (role.length() != 1) { // role is one letter
                    Error.processError(Error.ROLE_ELEMENT_ONE_LETTER_LONG, "Role is one character long");
                    return false;
                }

                if (role.charAt(0) > 'z' || role.charAt(0) < 'a') { // role is character
                    Error.processError(Error.ROLE_ELEMENT_IS_CHARACTER, "Role is a character");
                    return false;
                }

                if (role.charAt(0) > 'c') { // role too high
                    Error.processError(Error.ROLE_TOO_HIGH, "Role too high");
                    return false;
                }

                if (role.charAt(0) < 'c') { // role too low
                    Error.processError(Error.ROLE_TOO_LOW, "Role too low");
                    return false;
                }

                if (role.charAt(0) == 'c') { // If it has passed all the above tests: grant access
                    return true;
                }
            }
        }

        Error.processError(Error.INVALID_USERNAME_OR_PASSWORD, "Password or username entered doesn't match with any of the proper users in user.xml");
        return false;
    }


}
