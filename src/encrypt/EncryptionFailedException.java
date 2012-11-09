package encrypt;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 4, 2006
 * Time: 10:34:12 AM
 */
public class EncryptionFailedException extends Exception {

    public EncryptionFailedException(String message) {
        super(message);
    }

    public EncryptionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptionFailedException(Throwable cause) {
        super(cause);
    }

    public EncryptionFailedException() {
    }
}
