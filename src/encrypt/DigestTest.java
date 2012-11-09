package encrypt;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 4, 2006
 * Time: 10:33:19 AM
 */
public class DigestTest {

    public static String encrypt(String password)
            throws EncryptionFailedException {
        MessageDigest md = null;
        try {
            // Can be MD5 (128bit), SHA-1(160bit), SHA-256, SHA-384,SHA-512
            md = MessageDigest.getInstance("SHA-256"); //a
            byte data[] = md.digest(password.getBytes("UTF-8")); //b
            String hash = (new BASE64Encoder()).encode(data);
            return hash;
        } catch (NoSuchAlgorithmException e) { //Might be thrown by MessageDigest.getInstance
            throw new EncryptionFailedException(e.getMessage());
        } catch (UnsupportedEncodingException e) { //Might be thrown by password.getBytes
            throw new EncryptionFailedException(e.getMessage());
        }

    }


    public static void main(String[] args) {
        try {
            System.out.println(encrypt("kuku"));
            System.out.println(encrypt("kuku11"));
            System.out.println(encrypt("kuku22"));
            System.out.println(encrypt("kuku"));
            System.out.println(encrypt("kuku11"));
            System.out.println(encrypt("kuku22"));
        } catch (EncryptionFailedException ex) {
            ex.printStackTrace();
        }
    }
}
