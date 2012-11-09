package reference.soft;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 11:08:38 AM
 */
// A form of memory leak.
 //
 // It try to be clever and cache the buffer in an instance field to reduce memory churn.
 // The cause of the problem in LeakyChecksum is that the buffer is logically local to
 // the getFileChecksum() operation, but its lifecycle has been artificially prolonged
 // by promoting it to an instance field. As a result, the class has to manage the
 // lifecycle of the buffer itself rather than letting the JVM do it.

 // BAD CODE - DO NOT EMULATE
 // solution CachingCheckSum
 public class LeakyCheckSum {
     private byte[] byteArray;

     public synchronized int getFileChecksum(String fileName) {
         int len = getFileSize(fileName);
         if (byteArray == null || byteArray.length < len) {
             byteArray = new byte[len];
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