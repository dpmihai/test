package instancechecker;

/**
 * User: mihai.panaitescu
 * Date: 26-Nov-2009
 * Time: 11:21:23
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Prevents two instances of the application from starting at the same time.
 *   Usage
 *       if (!InstanceChecker.INSTANCE.onlyInstance()) {
            // Die!
         }
 * @author Mlk
 * http://ihaztehcodez.michael-lloyd-lee.me.uk/2009/11/preventing-multiple-instances-of.html?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed%3A+IHazTehCodez+%28i+haz+teh+codez%29
 *
 */
public final class InstanceChecker {
    /** A singleton instance. */
    public static final InstanceChecker INSTANCE = new InstanceChecker();
    /** The file channel to be locked. */
    private FileChannel channel;
    /** The lock on the file. */
    private FileLock lock;
    /** The file to be locked. */
    private final File file = new File(new File(System.getProperty("user.home")), "/APPLICATION_NAME.lock");
    /** Logger. */
    private Logger log = Logger.getLogger(getClass().getName());



    /** ctor. */
    private InstanceChecker() {    }

    /** Is this the only instance of this application currently executing.
     *
     * @return should the application be allowed to start up.
     */
    public boolean onlyInstance() {

        try {
            channel = new RandomAccessFile(file, "rw").getChannel();

            try {
                lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                return false;
            }
            if (lock == null) {
                return false;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    close();
                }
            });
        } catch (IOException e) {
            log.log(Level.WARNING, "Failed to lock file: " + file, e);
            return false;
        }
        return true;
    }

    /** Unlocks the file. */
    private void close() {
        try {
            if (lock != null) {
                lock.release();
            }
            if (channel != null) {
                channel.close();
            }

            file.delete();
        } catch (final IOException e) {
            System.err.println("Failed to unlock file!");
        }
    }

}
