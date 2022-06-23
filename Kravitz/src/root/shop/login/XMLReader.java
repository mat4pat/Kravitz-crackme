package root.shop.login;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import root.main.Error;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLReader {

    private static final String TEMP_FILE_NAME = "C:\\Users\\Public\\temp.temp";

    public static User.STATUS getUsersFromUsersXML() {

        User.getUsers().clear();
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc;
            try {
                 doc = db.parse(new File(TEMP_FILE_NAME));

            } catch(IOException e) {
                Error.processError(Error.TEMP_TEMP_MISSING, "temp.temp is probably missing, otherwise, regular IOException.");
                return User.STATUS.EXCEPTION_THROWN;

            } catch(SAXException e) {
                Error.processError(Error.XML_SYNTAX_ERROR, "SAXException while parsing Users.XML, XML parsing error.");
                return User.STATUS.EXCEPTION_THROWN;

            }

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            String nodeName; // nodeName refers to the <userInfo>
            nodeName = doc.getDocumentElement().getNodeName();
            if (!nodeName.equals("userInfo")) {
                Error.processError(Error.OTHER, "Warning: userInfo element is missing, might cause issues with multiple <user> elements.");
            }

            // get <staff>
            NodeList list = doc.getElementsByTagName("user");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    String username, password, role = null;

                    try {
                        username = element.getElementsByTagName("username").item(0).getTextContent();
                    } catch (Exception e) {
                        Error.processError(Error.XML_SYNTAX_ERROR, "Exception regarding element in XML: 'username' at line " + e.getStackTrace()[0].getLineNumber());
                        return User.STATUS.EXCEPTION_THROWN;
                    }

                    try {
                        password = element.getElementsByTagName("password").item(0).getTextContent();
                    } catch (Exception e) {
                        Error.processError(Error.XML_SYNTAX_ERROR, "Exception regarding element in XML: 'password' at line " + e.getStackTrace()[0].getLineNumber());
                        return User.STATUS.EXCEPTION_THROWN;
                    }

                    try {
                        role = element.getElementsByTagName("role").item(0).getTextContent();
                    } catch (Exception e) {
                        //Error.processError(Error.OTHER, "Exception regarding element in XML: 'role', not handling, part of crackme..."); // Generates too much logs in crackmelogs.txt, annoying..
                        // An idea of the crackme is for the user to create a unique user of his, and then realize he needs to add a role too.
                    }

                    User user = new User(username, password, role);
                    User.getUsers().add(user);

                }
            }

        } catch (ParserConfigurationException e) {
            Error.processError(Error.OTHER, "ParserConfigurationException thrown at line " + e.getStackTrace()[0].getLineNumber());
            return User.STATUS.EXCEPTION_THROWN;
        }
        return User.STATUS.ALL_WELL;
    }

}