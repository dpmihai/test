package annotation.validation;

import java.lang.annotation.Annotation;
import annotation.MinLength;

/**
 * A validator for the {@link MinLength MinLength} constraint.
 * @see MinLength
 *
 * @author Anders Holmgren
 */
public class MinLengthValidator extends AbstractConstraintValidator {
    /**
     * Constructs a MinLengthValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public MinLengthValidator() {
        super(MinLength.class);
    }

    public ConstraintViolation validate(Object objValue, Annotation constraint) {
        ensureConstraintIsSupported(constraint);

        MinLength ml = (MinLength)constraint;
        ConstraintViolation result = null;

        String value = (objValue != null ? objValue.toString() : null);
        if (value != null && value.length() < ml.value()) {
            result = new ConstraintViolation(ml,
                "MinLength constraint violated. Length " + value.length() +
                " is less than min " + ml.value());
        }

        return result;
    }
}
