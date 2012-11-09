package reference.soft;

import java.lang.ref.SoftReference;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 11:13:31 AM
 */
public class CachingCheckSum {
    private SoftReference<byte[]> bufferRef;

     public synchronized int getFileChecksum(String fileName) {
         int len = getFileSize(fileName);
         byte[] byteArray = bufferRef.get();
         if (byteArray == null || byteArray.length < len) {
             byteArray = new byte[len];
             bufferRef = new SoftReference<byte[]>(byteArray);
         }
         readFileContents(fileName, byteArray);
         // calculate checksum and return it
         return 0;
     }

    private int getFileSize(String fileName) {
        return 0;
    }

    private void readFileContents(String fileName, byte[] byteArray) {

    }
}
