package md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 16, 2006
 * Time: 10:34:01 AM
 */

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        File f = new File("E:\\Public\\Documents\\framework.pdf");
        InputStream is = new FileInputStream(f);
        byte[] buffer = new byte[8192];
        int read = 0;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();

            //The last step which is often important, however isn't required in all cases
            // is to take the finished result of the MD5 calculation, and turn it into some
            // manageable format. By default, the MessageDigest class provides a byte[]
            // representing the finished calculation. It is common practice in the modern
            // tech world to throw MD5 data around as a hexadecimal text string. I've seen
            // common solutions using Apache commons libraries
            // (such as the org.apache.commons.codec.binary.Hex.encodeHex(byte[])  method in
            // the Commons Codec  project), although using java.lang.BigInteger  it is possible
            // to perform the calculation (albeit possibly at a higher processing expense) -
            // the only trick with BigInteger is knowing which bit represents the sign bit
            // (hence the '1' in the following code):
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            System.out.println("MD5: " + output);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
            }
        }
    }

}
