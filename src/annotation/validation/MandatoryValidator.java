package annotation.validation;

import java.lang.annotation.Annotation;
import annotation.Mandatory;

/**
 * A validator for the {@link Mandatory Mandatory} constraint.
 * @see Mandatory
 *
 * @author Anders Holmgren
 */
public class MandatoryValidator extends AbstractConstraintValidator {
    /**
     * Constructs a MandatoryValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public MandatoryValidator() {
        super(Mandatory.class);
    }

    public ConstraintViolation validate(Object objValue, Annotation constraint) {
        ensureConstraintIsSupported(constraint);

        /*
         * If the value passed in came from a primitive property then it will
         * never be null anyway, so there is no need to attempt to determine
         * whether the constraint was applied to a primitive type.
         */
        ConstraintViolation result = null;

        if (objValue == null) {
            result = new ConstraintViolation(constraint,
                "Mandatory constraint violated.");
        }

        return result;
    }
}
