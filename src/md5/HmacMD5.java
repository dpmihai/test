package md5;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 10, 2009
 * Time: 10:47:01 AM
 */

import javax.crypto.*;

public class HmacMD5 {


    /**
     * This program demonstrates how to generate a secret-key object for
     * HMAC-MD5, and initialize an HMAC-MD5 object with it.
     * 
     * read more about hash functions here
     * http://www.javacodegeeks.com/2012/02/introduction-to-strong-cryptography-p1.html
     */

    public static void main(String[] args) throws Exception {

        // Generate secret key for HMAC-MD5
        KeyGenerator kg = KeyGenerator.getInstance("HmacMD5");
        SecretKey sk = kg.generateKey();

        // Get instance of Mac object implementing HMAC-MD5, and
        // initialize it with the above secret key
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(sk);
        byte[] result = mac.doFinal("Hi There".getBytes());
        System.out.println("key="+sk.getEncoded());
        System.out.println("result=" + result);


    }

}
