package annotation.validation;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 2:37:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidationException extends Exception {

    public ValidationException() {
    }

    public ValidationException(ConstraintViolation c) {
        super(c.getMessage());
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
