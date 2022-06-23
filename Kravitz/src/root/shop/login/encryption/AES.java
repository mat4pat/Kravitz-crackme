package root.shop.login.encryption;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AES {

    private static final String Algo ="AES";

    private static final String SECRET_KEY = "FishHaveNoFeelin";
    private byte[] keyValue;

    /**
     * Since we are using only a single and only key: 'FishHaveNoFeelin'
     * It's very useful to just make this a Singeleton class,
     * because the construcor create an AES object for a key.
     */

    private static AES instance = null;

    public static AES getInstance() {
        if (instance==null) {
            instance = new AES(SECRET_KEY);
        }
        return instance;
    }

    private AES (String key) {

        keyValue = key.getBytes();
    }

    private Key generateKey() {
        return new SecretKeySpec(keyValue, Algo);
    }

    public String encrypt(String Data) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(Algo);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);

        return encryptedValue;

    }

    public String decrypt(String encryptedData) throws DecryptionException {

        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(Algo);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);

            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String (decValue);

            return decryptedValue;

        } catch(Exception e) {
            throw new DecryptionException("Decryption error has occurred, very likely due to inappropriate input.");
        }



    }


}
