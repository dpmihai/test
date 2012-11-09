package swingx;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 11, 2008
 * Time: 3:02:03 PM
 */
public class InvalidException extends Exception {

    public InvalidException() {
        super();
    }

    public InvalidException(String message) {
        super(message);
    }

    public InvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidException(Throwable cause) {
        super(cause); 
    }
}
