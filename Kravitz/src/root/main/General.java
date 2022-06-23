package root.main;

import java.io.*;

public class General {

    public static StringBuilder readFileAsString(String filePath) {

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) { // try with resources will close br regardless.

            StringBuilder data = new StringBuilder();
            // This can throw FileNotFoundException

            String st;

            while ((st = br.readLine()) != null) {
                data.append(st).append("\n");
            }

            return data;

        } catch(FileNotFoundException e) {
            Error.processError(Error.OTHER, "FileNotFoundException thrown in General.readFileAsString() at line " + e.getStackTrace()[0].getLineNumber());
        }
        catch(IOException e) {
            Error.processError(Error.OTHER, "IOException thrown in General.readFileAsString() at line " + e.getStackTrace()[0].getLineNumber());
        }

        return null;
    }
}
