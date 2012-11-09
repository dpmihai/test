package annotation.validation;

import java.lang.annotation.Annotation;
import annotation.MaxLength;

/**
 * A validator for the {@link MaxLength MaxLength} constraint.
 * @see MaxLength
 *
 * @author Anders Holmgren
 */
public class MaxLengthValidator extends AbstractConstraintValidator {
    /**
     * Constructs a MaxLengthValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public MaxLengthValidator() {
        super(MaxLength.class);
    }

    public ConstraintViolation validate(Object objValue, Annotation constraint) {
        ensureConstraintIsSupported(constraint);

        MaxLength ml = (MaxLength)constraint;
        ConstraintViolation result = null;

        String value = (objValue != null ? objValue.toString() : null);
        if (value != null && value.length() > ml.value()) {
            result = new ConstraintViolation(ml,
                "MaxLength constraint violated. Length " + value.length() +
                " is greater than max " + ml.value());
        }

        return result;
    }
}
