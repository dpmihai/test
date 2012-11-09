package encrypt;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 5, 2007
 * Time: 2:05:39 PM
 */

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class DESSecure {

    static String xform = "DES/CBC/PKCS5Padding";
        // Generate a secret key
    static KeyGenerator kg = null;
    static SecretKey key;

    static {
        try {
            kg = KeyGenerator.getInstance("DES");
            kg.init(56); // 56 is the keysize. Fixed for DES
            key = kg.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static byte[] iv =
            {0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d};

    private static byte[] encrypt(byte[] inpBytes,
                                  SecretKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ips);
        return cipher.doFinal(inpBytes);
    }

    private static byte[] decrypt(byte[] inpBytes,
                                  SecretKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ips);
        return cipher.doFinal(inpBytes);
    }
   

    public static void main(String[] unused) throws Exception {
        String xform = "DES/CBC/PKCS5Padding";
        // Generate a secret key
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(56); // 56 is the keysize. Fixed for DES
        SecretKey key = kg.generateKey();

        byte[] dataBytes = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
                //"J2EE Security for Servlets, EJBs and Web Services".getBytes();

        byte[] encBytes = encrypt(dataBytes, key, xform);
        byte[] decBytes = decrypt(encBytes, key, xform);

        boolean expected = java.util.Arrays.equals(dataBytes, decBytes);
        System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));        
        System.out.println("enc=" + new String(encBytes));
        System.out.println("dec=" + new String(decBytes));
    	        
    }
}