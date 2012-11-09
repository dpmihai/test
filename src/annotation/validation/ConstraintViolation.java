package annotation.validation;

import java.lang.annotation.Annotation;

/**
 * Represents a violation of a constraint. Captures the constraint (Annotation)
 * that was violated and a message describing the violation.
 * <p>
 * Note there may be violations of several constraints during the validation of
 * a property. All these violations are captured as separate ConstraintViolation
 * objects and typically rolled up as part of an overall validation result.
 * </p>
 * @see PropertyValidationErrors
 *
 * @author Anders Holmgren
 */
public class ConstraintViolation {
    private Annotation constraint;
    
    private String message;

    /**
     * Creates a ConstraintViolation with the violated constraint and 
     * message describing the violation
     * @throws IllegalArgumentException if constraint is null or message is null
     */
    public ConstraintViolation(Annotation constraint, String message) {
        if (constraint == null)
            throw new IllegalArgumentException("null is not a legal value for constraint");
        if (message == null)
            throw new IllegalArgumentException("null is not a legal value for message");
        
        this.constraint = constraint;
        this.message = message;
    }

    /**
     * The constraint that was violated
     */
    public Annotation getConstraint() {
        return constraint;
    }

    /**
     * Text describing the violation. Note applications do not necessarily need
     * to make use of this message, as they have enough information to create
     * their own messages. In particular they know the constraint that was 
     * violated and the value that violated the constraint. As such this message
     * can be considered a default message and is useful for logging etc.
     */
    public String getMessage() {
        return message;
    }
}
